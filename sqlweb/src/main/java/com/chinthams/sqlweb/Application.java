/**
 * 
 */
package com.chinthams.sqlweb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author SRCHINTH
 *
 */
@SpringBootApplication
public class Application implements Constants {
	private static Properties appProperties = new Properties();
	private static Properties srvProperties = new Properties();

	static {
		srvProperties.put(server_port, default_port);
	}

	/**
	 * @return appProperties
	 */
	public static Properties getAppProperties() {
		return appProperties;
	}

	/**
	 * @param args
	 */
	private static void loadProperties(String[] args) {
		FileReader fr = null;
		if (args != null && args.length > 0) {
			try {
				if (args[0] != null && args[0].trim().length() > 0) {
					File iFile = new File(args[0].trim());
					if (iFile.exists() && iFile.isFile() && iFile.canRead()) {
						fr = new FileReader(iFile);
						appProperties.load(fr);
						if (appProperties.containsKey(server_port)) {
							try {
								Integer.parseInt((String) appProperties.get(server_port));
								srvProperties.put(server_port, appProperties.get(server_port));
							} catch (NumberFormatException nfe) {
								System.out.println("Invalid overriding port");
							}
						}
					} else {
						System.out.println("File Not Found : " + iFile.getPath());
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fr != null) {
					try {
						fr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		loadProperties(args);
		SpringApplication app = new SpringApplication(Application.class);
		app.setDefaultProperties(srvProperties);
		app.run(args);
	}

}
