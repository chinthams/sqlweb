/**
 * 
 */
package com.chinthams.sqlweb.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
						// printDatabaseMetaData(con);
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

	private void printDatabaseMetaData(Connection con) {
		try {
			DatabaseMetaData dsmd = con.getMetaData();
			System.out.println("Product Name: " + dsmd.getDatabaseProductName());
			System.out.println("Catalog Term: " + dsmd.getCatalogTerm());
			System.out.println("Driver Name: " + dsmd.getDriverName());
			List<String> schemas = new ArrayList<String>();
			ResultSet rs = dsmd.getColumns(null, null, "%", "%");
			int cnt = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				for (int i=1; i<=cnt; i++) {
					System.out.print(rs.getObject(i));
					System.out.print("|");
				}
				System.out.println();
			}
			rs.close();

			List<String> tabTypes = new ArrayList<String>();
			rs = dsmd.getTableTypes();
			cnt = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				tabTypes.add(rs.getString(1));
			}
			rs.close();

			for (String schema : schemas) {
				System.out.println("Schema============<Start>: " + schema);
				rs = dsmd.getTables(null, schema, "%", tabTypes.toArray(new String[tabTypes.size()]));
				cnt = rs.getMetaData().getColumnCount();
				while (rs.next()) {
					for (int i = 1; i <= cnt; i++) {
						System.out.print(rs.getObject(i));
						System.out.print("|");
					}
					System.out.println();
				}
				rs.close();
				System.out.println("Schema============<End>: ");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
