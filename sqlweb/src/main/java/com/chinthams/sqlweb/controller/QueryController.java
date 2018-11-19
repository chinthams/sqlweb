/**
 * 
 */
package com.chinthams.sqlweb.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chinthams.sqlweb.vo.ColumnMetaData;
import com.chinthams.sqlweb.vo.QueryRequest;
import com.chinthams.sqlweb.vo.QueryResult;

/**
 * @author SRCHINTH
 *
 */
@RestController
public class QueryController {

	@Autowired
	private Map<String, DataSource> dataSources;

	@GetMapping("/")
	public String home() {
		return "Welcome to SQLWEB";
	}
	
	@GetMapping("/dataSources")
	public Set<String> getDataSources() {
		return dataSources.keySet();
	}

	@PutMapping(path = "/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QueryResult> getQuery(@RequestBody QueryRequest qRequest) {
		QueryResult qResult = new QueryResult();
		Statement st = null;
		ResultSet rs = null;
		try {
			String dsName = qRequest.getDataSource();
			String queryString = qRequest.getQueryString();
			if (dsName != null && dataSources.containsKey(dsName)) {
				DataSource ds = dataSources.get(dsName);
				Connection con = ds.getConnection();
				st = con.createStatement();
				rs = st.executeQuery(queryString);
				mapResult(qResult, rs);
			} else {
				qResult.setStatusCode(101);
				qResult.setStatusDescription("Invalid DataSource");
			}
		} catch (Exception ex) {
			qResult.setStatusDescription(ex.getMessage());
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return new ResponseEntity<QueryResult>(qResult, HttpStatus.OK);
	}

	private void mapResult(QueryResult qResult, ResultSet rs) {
		try {
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int colCnt = rsMetaData.getColumnCount();
			qResult.setColumnCount(colCnt);
			List<ColumnMetaData> resultMetaData = new ArrayList<ColumnMetaData>(colCnt);
			for (int column=1; column<=colCnt; column++) {
				resultMetaData.add(new ColumnMetaData(column, rsMetaData));
			}
			qResult.setResultMetaData(resultMetaData);
			qResult.setResult(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
