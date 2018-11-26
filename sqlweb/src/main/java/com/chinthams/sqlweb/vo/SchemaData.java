/**
 * 
 */
package com.chinthams.sqlweb.vo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author SRCHINTH
 *
 */
public class SchemaData {
	private String schemaName;
	private Map<String, List<String>> schemaObjects = new TreeMap<String, List<String>>();

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
	 * @return the schemaObjects
	 */
	public Map<String, List<String>> getSchemaObjects() {
		return schemaObjects;
	}

	/**
	 * @param schemaObjects the schemaObjects to set
	 */
	public void setSchemaObjects(Map<String, List<String>> schemaObjects) {
		this.schemaObjects = schemaObjects;
	}

}
