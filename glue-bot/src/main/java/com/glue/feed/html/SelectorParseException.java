package com.glue.feed.html;

/**
 * A checked exception that signals that an error has been reached unexpectedly
 * while parsing an HTML document.
 */
public class SelectorParseException extends Exception {

    private String cssQuery;

    public SelectorParseException(String msg) {
	this(msg, null);
    }

    public SelectorParseException(String msg, String cssQuery) {
	super(msg);
	this.cssQuery = cssQuery;
    }

    public String getCssQuery() {
	return cssQuery;
    }

}
