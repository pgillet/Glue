package com.glue.api.application;

import org.apache.http.impl.client.DefaultHttpClient;

public class GlueClientContext {

	private DefaultHttpClient httpClient;

	public DefaultHttpClient getHttpClient() {
		if (httpClient == null) {
			httpClient = createHttpClient();
		}

		return httpClient;
	}

	public DefaultHttpClient createHttpClient() {
		// SchemeRegistry schemeRegistry = new SchemeRegistry();
		// schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
		// .getSocketFactory()));

		// schemeRegistry.register(
		// new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

		// PoolingClientConnectionManager cm = new
		// PoolingClientConnectionManager(
		// schemeRegistry);

		// Increase max total connection to 200
		// cm.setMaxTotal(200);
		// Increase default max connection per route to 20
		// cm.setDefaultMaxPerRoute(20);
		
		// Increase max connections for localhost:80 to 50
		// URL baseURL = new URL(Configuration.getBaseUrl());
		// HttpHost targetHost = new HttpHost(baseURL.getHost(),
		// baseURL.getPort());
		// cm.setMaxPerRoute(new HttpRoute(targetHost), 50);

		DefaultHttpClient httpClient = new DefaultHttpClient(/*cm*/);

		return httpClient;
	}

}
