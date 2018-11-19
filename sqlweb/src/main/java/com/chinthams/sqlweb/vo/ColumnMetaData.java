/**
 * 
 */
package com.chinthams.sqlweb.vo;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author CHINTHAMS
 *
 */
public class ColumnMetaData {
	private int columnDisplaySize;
	private int columnType;
	private String columnTypeName;
	private String catalogName;
	private String columnClassName;
	private String columnLabel;
	private String columnName;
	private int precision;
	private int scale;
	private String schemaName;
	private String tableName;

	public ColumnMetaData(int column, ResultSetMetaData rsMetaData) {
		try {
			if (column > 0 && rsMetaData != null && rsMetaData.getColumnCount() >= column) {
				columnDisplaySize = rsMetaData.getColumnDisplaySize(column);
				columnType = rsMetaData.getColumnType(column);
				columnTypeName = rsMetaData.getColumnTypeName(column);
				catalogName = rsMetaData.getCatalogName(column);
				columnClassName = rsMetaData.getColumnClassName(column);
				columnLabel = rsMetaData.getColumnLabel(column);
				columnName = rsMetaData.getColumnName(column);
				precision = rsMetaData.getPrecision(column);
				scale = rsMetaData.getScale(column);
				schemaName = rsMetaData.getSchemaName(column);
				tableName = rsMetaData.getTableName(column);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @return the columnDisplaySize
	 */
	public int getColumnDisplaySize() {
		return columnDisplaySize;
	}

	/**
	 * @param columnDisplaySize the columnDisplaySize to set
	 */
	public void setColumnDisplaySize(int columnDisplaySize) {
		this.columnDisplaySize = columnDisplaySize;
	}

	/**
	 * @return the columnType
	 */
	public int getColumnType() {
		return columnType;
	}

	/**
	 * @param columnType the columnType to set
	 */
	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}

	/**
	 * @return the columnTypeName
	 */
	public String getColumnTypeName() {
		return columnTypeName;
	}

	/**
	 * @param columnTypeName the columnTypeName to set
	 */
	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	/**
	 * @return the catalogName
	 */
	public String getCatalogName() {
		return catalogName;
	}

	/**
	 * @param catalogName the catalogName to set
	 */
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	/**
	 * @return the columnClassName
	 */
	public String getColumnClassName() {
		return columnClassName;
	}

	/**
	 * @param columnClassName the columnClassName to set
	 */
	public void setColumnClassName(String columnClassName) {
		this.columnClassName = columnClassName;
	}

	/**
	 * @return the columnLabel
	 */
	public String getColumnLabel() {
		return columnLabel;
	}

	/**
	 * @param columnLabel the columnLabel to set
	 */
	public void setColumnLabel(String columnLabel) {
		this.columnLabel = columnLabel;
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return the precision
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * @return the scale
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @param schemaName the schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
