/**
 * 
 */
package com.chinthams.sqlweb;

import java.util.regex.Pattern;

/**
 * @author SRCHINTH
 *
 */
public interface Constants {
	String BLANK = "";
	String PREFIX_JDBC = "jdbc.";
	String SPLIT = "\\.";
	String default_port = "0";
	String server_port = "server.port";

	int STCODE_SUCCESS = 100;
	String STDESC_SUCCESS = "Success";
	int STCODE_INVALID_DATASOURCE = 101;
	String STDESC_INVALID_DATASOURCE = "Invalid DataSource";
	int STCODE_INVALID_QUERY = 102;
	String STDESC_INVALID_QUERY = "Invalid Query String";
	
	Pattern DML_PATTERN = Pattern.compile("^[^a-zA-Z]*?(CREATE|ALTER|DROP|INSERT|UPDATE|DELETE|EXECUTE).*$", Pattern.CASE_INSENSITIVE);
	
	
}
