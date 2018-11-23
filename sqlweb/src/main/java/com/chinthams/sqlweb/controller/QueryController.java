/**
 * 
 */
package com.chinthams.sqlweb.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chinthams.sqlweb.Constants;
import com.chinthams.sqlweb.vo.ColumnMetaData;
import com.chinthams.sqlweb.vo.QueryRequest;
import com.chinthams.sqlweb.vo.QueryResult;

/**
 * @author SRCHINTH
 *
 */
@RestController
public class QueryController implements Constants {

	private final Log logger = LogFactory.getLog(QueryController.class.getName());

	@Autowired
	private Map<String, DataSource> dataSources;

	/*
	@GetMapping("/")
	public String home() {
		logger.info("Inside home method");
		return "Welcome to SQLWEB";
	}
	*/
	
	@GetMapping("/dataSources")
	public Set<String> getDataSources() {
		logger.info("Inside getDataSources method");
		return dataSources.keySet();
	}

	@PostMapping(path = "/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QueryResult> getQuery(@RequestBody QueryRequest qRequest) {
		logger.info("Inside getQuery method");
		QueryResult qResult = new QueryResult();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			System.out.println(qRequest.getDataSource());
			System.out.println(qRequest.getQueryString());
			String dsName = qRequest.getDataSource() != null ? qRequest.getDataSource().trim() : BLANK;
			String queryString = qRequest.getQueryString() != null ? qRequest.getQueryString().trim() : BLANK;
			qResult.setDataSource(dsName);
			qResult.setQueryString(queryString);
			if (dataSources.containsKey(dsName)) {
				if (isValidQueryString(queryString)) {
					DataSource ds = dataSources.get(dsName);
					con = ds.getConnection();
					st = con.createStatement();
					rs = st.executeQuery(queryString);
					mapResult(qResult, rs);
					qResult.setStatusCode(STCODE_SUCCESS);
					qResult.setStatusDescription(STDESC_SUCCESS);
				} else {
					qResult.setStatusCode(STCODE_INVALID_QUERY);
					qResult.setStatusDescription(STDESC_INVALID_QUERY);
				}
			} else {
				qResult.setStatusCode(STCODE_INVALID_DATASOURCE);
				qResult.setStatusDescription(STDESC_INVALID_DATASOURCE);
			}
		} catch (Exception ex) {
			qResult.setStatusDescription(ex.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return new ResponseEntity<QueryResult>(qResult, HttpStatus.OK);
	}

	private boolean isValidQueryString(String queryString) {
		if (queryString != null && queryString.length() > 0) {
			return true;
		}
		return false;
	}

	private void mapResult(QueryResult qResult, ResultSet rs) {
		try {
			// map Resultset Metadata
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int colCnt = rsMetaData.getColumnCount();
			qResult.setColumnCount(colCnt);
			List<ColumnMetaData> resultMetaData = new ArrayList<ColumnMetaData>(colCnt);
			ColumnMetaData colData = null;
			List<Integer> generalTypes = new ArrayList<Integer>();
			Object[] rowTemplate = new Object[colCnt];
			for (int column = 1; column <= colCnt; column++) {
				colData = new ColumnMetaData(column, rsMetaData);
				switch (colData.getColumnType()) {
					case Types.ARRAY:
					case Types.BINARY:
					case Types.BLOB:
					case Types.CLOB:
					case Types.LONGVARBINARY:
					case Types.NCLOB:
					case Types.VARBINARY:
					case Types.STRUCT:
					case Types.REF_CURSOR:
					case Types.OTHER:
						rowTemplate[column - 1] = colData.getColumnTypeName();
						break;
					default:
						generalTypes.add(column);
						break;
				}
				resultMetaData.add(colData);
			}
			qResult.setResultMetaData(resultMetaData);

			// Map Resultset
			int rowCount = 0;
			List<Object[]> rowData = new ArrayList<Object[]>();
			while (rs.next()) {
				Object[] row = new Object[colCnt];
				// Copy the default Values
				System.arraycopy(rowTemplate, 0, row, 0, colCnt);
				for (int i : generalTypes) {
					row[i-1] = rs.getObject(i);
				}
				rowCount++;
				rowData.add(row);
			}
			qResult.setResult(rowData);
			qResult.setResultCount(rowCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
