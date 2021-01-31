package com.kelly.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	private Properties prop = null;
	public static final String CONFIG_FILE = "config.properties";	// config檔名
	
	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}
	
	/**
	 * 讀取config檔的內容
	 */
	public Configuration(){
		prop = new Properties();
		InputStream input = null;

		try {
			String propFileName = CONFIG_FILE;
			input = getClass().getClassLoader().getResourceAsStream(propFileName);

			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
