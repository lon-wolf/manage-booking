package com.booking.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesContext {

	private static final Logger logger = LogManager.getLogger(PropertiesContext.class);

	public static Properties properties = new Properties();

	static {
		initializeProperty();
	}

	private static void initializeProperty() {
		URL url = PropertiesContext.class.getClassLoader().getResource("config.properties");
		if (url != null) {
			logger.info("Loading properties from config.properties file.");
			try {
				properties.load(new InputStreamReader(url.openStream()));
			} catch (IOException e) {
				logger.error("Error in Loading properties from config.properties file", e);
			}

		}
	}
}
