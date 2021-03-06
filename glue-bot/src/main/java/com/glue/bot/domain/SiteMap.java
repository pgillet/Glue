package com.glue.bot.domain;

import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * An object that describes a site map accessible for Web scraping.
 * 
 * @see SiteMapBuilder
 * 
 * @author pgillet
 * 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SiteMap {

    public static final int LINK_DISABLED = 0;

    public static final int EMPTY_LIST = 1;

    public static final int DEFAULT_MAX_PAGES = 50;

    private String frontUrl;

    private String listItemSelector;

    private String nextPageSelector;

    private String endOfDataCondition;

    private String venuePage;

    public SiteMap() {
    }

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

    public String getFrontUrl() {
	return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
	this.frontUrl = frontUrl;
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
