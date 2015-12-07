package com.glue.bot;

/**
 * A URL filter that tests whether or not the specified URL starts with a base
 * URL.
 * 
 * @author pgillet
 * 
 */
public class BaseURLFilter implements URLFilter {

    private String baseUrl;

    public BaseURLFilter(String baseUrl) {
	this.baseUrl = baseUrl;
    }

    @Override
    public boolean accept(String url) {
	return url.startsWith(baseUrl);
    }

    public String getBaseUrl() {
	return baseUrl;
    }

}
