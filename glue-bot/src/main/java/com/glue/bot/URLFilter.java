package com.glue.bot;

/**
 * A filter for URLs.
 * 
 * @author pgillet
 * 
 */
public interface URLFilter {

    /**
     * Tests whether or not the specified URL should be included in a URL list.
     * 
     * @param url
     *            The URL to be tested
     * @return true if and only if the URL should be included
     */
    boolean accept(String url);

}
