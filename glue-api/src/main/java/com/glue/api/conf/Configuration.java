package com.glue.api.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

	private static final String BASE_URL = "base.url";
	private static final String PROPERTIES_FILENAME = "/conf.properties";
	private static Properties props;

	static {
		props = new Properties();
		InputStream in = Configuration.class
				.getResourceAsStream(PROPERTIES_FILENAME);
		try {
			props.load(in);
		} catch (IOException e) {
			throw new IllegalStateException();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// Ignore
			}
		}
	}

	public static String getBaseUrl() {
		return props.getProperty(BASE_URL);
	}

}
