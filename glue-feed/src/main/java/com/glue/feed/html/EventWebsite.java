package com.glue.feed.html;

public class EventWebsite {

    private SiteMap siteMap;
    private EventDetailsPage details;

    public EventWebsite() {
    }

    public EventWebsite(SiteMap siteMap, EventDetailsPage details) {
	this.siteMap = siteMap;
	this.details = details;
    }

    public SiteMap getSiteMap() {
	return siteMap;
    }

    public void setSiteMap(SiteMap siteMap) {
	this.siteMap = siteMap;
    }

    public EventDetailsPage getDetails() {
	return details;
    }

    public void setDetails(EventDetailsPage details) {
	this.details = details;
    }

}
