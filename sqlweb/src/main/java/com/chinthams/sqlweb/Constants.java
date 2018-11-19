/**
 * 
 */
package com.chinthams.sqlweb;

import java.text.MessageFormat;

/**
 * @author SRCHINTH
 *
 */
public interface Constants {
	String PREFIX_JDBC = "jdbc.";
	String SPLIT = ".";
	String default_port = "0";
	String server_port = "server.port";
	MessageFormat MF_JDBC_DRIVER = new MessageFormat("jdbc.{0}.driverClassName");
	MessageFormat MF_JDBC_URL = new MessageFormat("jdbc.{0}.url");
	MessageFormat MF_JDBC_USER = new MessageFormat("jdbc.{0}.username");
	MessageFormat MF_JDBC_PASSWORD = new MessageFormat("jdbc.{0}.password");
	
}
