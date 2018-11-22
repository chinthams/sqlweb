/**
 * 
 */
package com.chinthams.sqlweb.config;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chinthams.sqlweb.Application;
import com.chinthams.sqlweb.Constants;

/**
 * @author SRCHINTH
 *
 */
@Configuration
public class AppConfig implements Constants {

	private final Log logger = LogFactory.getLog(AppConfig.class.getName());

	@Bean
	public Map<String, DataSource> getDataSources() {
		Map<String, DataSource> dataSources = new TreeMap<String, DataSource>();
		Properties properties = Application.getAppProperties();
		loadDataSources(properties, dataSources);
		return dataSources;
	}
	
	private void loadDataSources(Properties properties, Map<String, DataSource> dataSources) {
		Map<String, Properties> dsPropsMap = new HashMap<String, Properties>();
		Properties props = null;
		for (String propKey : properties.stringPropertyNames()) {
			if (propKey.startsWith(PREFIX_JDBC)) {
				String[] keys = propKey.split(SPLIT);
				/*
				 * jdbc.<dsName>.driverClassName
				 * jdbc.<dsName>.url
				 * jdbc.<dsName>.username
				 * jdbc.<dsName>.password
				 * jdbc.<dsName>.validationQuery
				 * jdbc.<dsName>.testOnCreate
				 */
				if (keys != null && keys.length == 3) {
					String dsName = keys[1];
					if (dsPropsMap.containsKey(dsName)) {
						props = dsPropsMap.get(dsName);
					} else {
						props = new Properties();
						dsPropsMap.put(dsName, props);
					}
					props.put(keys[2], properties.get(propKey));
				}
			}
		}
		for (Entry<String, Properties> dsEntry : dsPropsMap.entrySet()) {
			try {
				DataSource ds = BasicDataSourceFactory.createDataSource(dsEntry.getValue());
				boolean isCreated = false;
				if (ds != null) {
					Connection con = ds.getConnection();
					if (con != null) {
						dataSources.put(dsEntry.getKey(), ds);
						isCreated = true;
					}
				}
				if (isCreated) {
					logger.info("Successfully created DataSource for - " + dsEntry.getKey());
				} else {
					logger.info("Unable to create DataSource for - " + dsEntry.getKey());
				}
			} catch (Exception e) {
				logger.info("Unable to create DataSource for - " + dsEntry.getKey(), e);
			}
		}
	}

}
