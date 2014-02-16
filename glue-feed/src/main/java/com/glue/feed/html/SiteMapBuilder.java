package com.glue.feed.html;


public class SiteMapBuilder {

    private SiteMap siteMap;

    public SiteMapBuilder(String baseUri) {
	this.siteMap = new SiteMap(baseUri);
    }

    public SiteMapBuilder next(String cssQuery) {
	siteMap.setNextPageSelector(cssQuery);
	return this;
    }

    public SiteMapBuilder last(String cssQuery) {
	siteMap.setEndOfDataCondition(cssQuery);
	return this;
    }

    public SiteMapBuilder list(String cssQuery) {
	siteMap.setListSelector(cssQuery);
	return this;
    }
    
    public SiteMapBuilder maxPages(int num) {
	siteMap.setMaxPages(num);
	return this;
    }

    public SiteMap build() {
	return siteMap;
    }

}
