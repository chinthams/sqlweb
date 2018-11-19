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
				// e.g. jdbc.<dsName>.driver/url/user/password
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
				if (ds != null) {
					Connection con = ds.getConnection();
					if (con != null) {
						dataSources.put(dsEntry.getKey(), ds);
					}
				}
			} catch (Exception e) {
				System.out.println("Unable to create DataSource for :" + dsEntry.getKey());
				e.printStackTrace();
			}
		}
	}

}
