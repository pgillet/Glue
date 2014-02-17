package com.glue.feed.html;

/**
 * An object that describes a site map accessible for Web scraping.
 * 
 * @see SiteMapBuilder
 * 
 * @author pgillet
 * 
 */
public class SiteMap {

    public static final int LINK_DISABLED = 0;

    public static final int EMPTY_LIST = 1;

    public static final int DEFAULT_MAX_PAGES = 50;

    private String baseUri;

    private String listSelector;

    private String nextPageSelector;

    private String endOfDataCondition;

    private int maxPages = DEFAULT_MAX_PAGES;

    public SiteMap(String baseUri) {
	this.baseUri = baseUri;
    }

    public String getListSelector() {
	return listSelector;
    }

    public void setListSelector(String cssQuery) {
	this.listSelector = cssQuery;
    }

    public String getNextPageSelector() {
	return nextPageSelector;
    }

    public void setNextPageSelector(String cssQuery) {
	this.nextPageSelector = cssQuery;
    }

    public int getMaxPages() {
	return maxPages;
    }

    public void setMaxPages(int maxPages) {
	this.maxPages = maxPages;
    }

    public String getBaseUri() {
	return baseUri;
    }

    public String getEndOfDataCondition() {
	return endOfDataCondition;
    }

    public void setEndOfDataCondition(String cssQuery) {
	this.endOfDataCondition = cssQuery;
    }

}
