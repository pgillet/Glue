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

    private String frontUrl;

    private String listItemSelector;

    private String nextPageSelector;

    private String endOfDataCondition;

    private String venuePage;

    private int maxPages = DEFAULT_MAX_PAGES;

    public SiteMap(String frontUrl) {
	this.frontUrl = frontUrl;
    }

    public String getListItemSelector() {
	return listItemSelector;
    }

    public void setListItemSelector(String cssQuery) {
	this.listItemSelector = cssQuery;
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

    public String getFrontUrl() {
	return frontUrl;
    }

    public String getEndOfDataCondition() {
	return endOfDataCondition;
    }

    public void setEndOfDataCondition(String cssQuery) {
	this.endOfDataCondition = cssQuery;
    }

    public String getVenuePage() {
	return venuePage;
    }

    public void setVenuePage(String cssQuery) {
	this.venuePage = cssQuery;
    }
}
