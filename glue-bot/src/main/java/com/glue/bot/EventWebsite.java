package com.glue.bot;

public class EventWebsite {

    private SiteMap siteMap;
    private EventSelectors details;

    public EventWebsite() {
    }

    public EventWebsite(SiteMap siteMap, EventSelectors details) {
	this.siteMap = siteMap;
	this.details = details;
    }

    public SiteMap getSiteMap() {
	return siteMap;
    }

    public void setSiteMap(SiteMap siteMap) {
	this.siteMap = siteMap;
    }

    public EventSelectors getDetails() {
	return details;
    }

    public void setDetails(EventSelectors details) {
	this.details = details;
    }

}
