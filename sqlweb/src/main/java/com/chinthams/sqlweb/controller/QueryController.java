/**
 * 
 */
package com.chinthams.sqlweb.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import com.chinthams.sqlweb.vo.SchemaData;

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

	@PostMapping(path = "/dataSourceInfo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Map<String, SchemaData>>> getDataSourceInfo(@RequestBody QueryRequest qRequest) {
		logger.info("Inside getDataSourceInfo method");
		Map<String, Map<String, SchemaData>> result = new TreeMap<String, Map<String, SchemaData>>();
		Connection con = null;
		ResultSet rs = null;
		try {
			System.out.println(qRequest.getDataSource());
			String dsName = qRequest.getDataSource() != null ? qRequest.getDataSource().trim() : BLANK;
			if (dataSources.containsKey(dsName)) {
				DataSource ds = dataSources.get(dsName);
				con = ds.getConnection();
				DatabaseMetaData dsmd = con.getMetaData();
				rs = dsmd.getTables(null, null, "%", null);
				Map<String, SchemaData> catMap = null;
				SchemaData schemaData = null;
				List<String> tabNames = null;
				while (rs.next()) {
					String catalogue = rs.getString("TABLE_CAT");
					String schema = rs.getString("TABLE_SCHEM");
					String tabName = rs.getString("TABLE_NAME");
					String tabType = rs.getString("TABLE_TYPE");
					if (catalogue == null) {
						catalogue = "Default";
					}
					if (schema == null) {
						schema = "Default";
					}
					if (tabType == null) {
						tabType = "Default";
					}
					
					if (result.containsKey(catalogue)) {
						catMap = result.get(catalogue);
					} else {
						catMap = new TreeMap<String, SchemaData>();
						result.put(catalogue, catMap);
					}
					if (catMap.containsKey(schema)) {
						schemaData = catMap.get(schema);
					} else {
						schemaData = new SchemaData();
						schemaData.setSchemaName(schema);
						catMap.put(schema, schemaData);
					}
					if (schemaData.getSchemaObjects().containsKey(tabType)) {
						tabNames = schemaData.getSchemaObjects().get(tabType); 
					} else {
						tabNames = new ArrayList<String>();
						schemaData.getSchemaObjects().put(tabType, tabNames);
					}
					tabNames.add(tabName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		return new ResponseEntity<Map<String, Map<String, SchemaData>>>(result, HttpStatus.OK);
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
		if (queryString != null && queryString.length() > 0 && !DML_PATTERN.matcher(queryString).matches()) {
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
