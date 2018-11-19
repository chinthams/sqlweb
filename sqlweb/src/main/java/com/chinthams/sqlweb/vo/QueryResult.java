/**
 * 
 */
package com.chinthams.sqlweb.vo;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author SRCHINTH
 *
 */
public class QueryResult {
	private int statusCode;
	private String statusDescription;
	private int columnCount;
	private List<ColumnMetaData> resultMetaData;
	private ResultSet result;

	/**
	 * @return the columnCount
	 */
	public int getColumnCount() {
		return columnCount;
	}

	/**
	 * @param columnCount the columnCount to set
	 */
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	/**
	 * @return the resultMetaData
	 */
	public List<ColumnMetaData> getResultMetaData() {
		return resultMetaData;
	}

	/**
	 * @param resultMetaData the resultMetaData to set
	 */
	public void setResultMetaData(List<ColumnMetaData> resultMetaData) {
		this.resultMetaData = resultMetaData;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the statusDescription
	 */
	public String getStatusDescription() {
		return statusDescription;
	}

	/**
	 * @param statusDescription the statusDescription to set
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	/**
	 * @return the result
	 */
	public ResultSet getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(ResultSet result) {
		this.result = result;
	}

}
