/**
 * 
 */
package com.chinthams.sqlweb.vo;

import java.util.List;

/**
 * @author SRCHINTH
 *
 */
public class QueryResult extends QueryRequest {
	private int columnCount;
	private List<Object[]> result;
	private int resultCount;
	private List<ColumnMetaData> resultMetaData;
	private int statusCode;
	private String statusDescription;

	/**
	 * @return the columnCount
	 */
	public int getColumnCount() {
		return columnCount;
	}

	/**
	 * @return the result
	 */
	public List<Object[]> getResult() {
		return result;
	}

	/**
	 * @return the resultCount
	 */
	public int getResultCount() {
		return resultCount;
	}

	/**
	 * @return the resultMetaData
	 */
	public List<ColumnMetaData> getResultMetaData() {
		return resultMetaData;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @return the statusDescription
	 */
	public String getStatusDescription() {
		return statusDescription;
	}

	/**
	 * @param columnCount the columnCount to set
	 */
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<Object[]> result) {
		this.result = result;
	}

	/**
	 * @param resultCount the resultCount to set
	 */
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

	/**
	 * @param resultMetaData the resultMetaData to set
	 */
	public void setResultMetaData(List<ColumnMetaData> resultMetaData) {
		this.resultMetaData = resultMetaData;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @param statusDescription the statusDescription to set
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

}
