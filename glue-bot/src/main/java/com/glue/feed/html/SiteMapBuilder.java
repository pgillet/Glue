package com.glue.feed.html;

/**
 * A builder for SiteMap objects.
 * 
 * @see SiteMap
 * @author pgillet
 *
 */
public class SiteMapBuilder {

    private SiteMap siteMap;

    protected SiteMapBuilder() {
    }

    /**
     * Constructs a new builder for a SiteMap object. The given url is typically
     * the link to the first page of list items.
     * 
     * @param frontUrl
     */
    public static SiteMapBuilder newSiteMap(String frontUrl) {
	SiteMapBuilder builder = new SiteMapBuilder();
	builder.siteMap = new SiteMap(frontUrl);

	return builder;
    }

    /**
     * Required for multiple pages listing multiple events (pagination). Sets
     * the Selector CSS query that should match the unambiguous ancestor of the
     * link to the next page, if any.
     * 
     * @param cssQuery
     *            a Selector CSS-like query
     * @return this builder
     * @see the query syntax documentation in <a
     *      href="http://jsoup.org/apidocs/org/jsoup/select/Selector.html"
     *      >Selector</a>
     */
    public SiteMapBuilder next(String cssQuery) {
	siteMap.setNextPageSelector(cssQuery);
	return this;
    }

    /**
     * Sets the Selector CSS query that specifies the end of pagination
     * condition when multiple pages list multiple events. This setting is
     * complementary to {@link #next(String)} but not necessary, as the last
     * page is reached either when there are no more elements matching the
     * {@link #next(String)} condition, or when elements match the given
     * selector.
     * 
     * @param cssQuery
     *            a Selector CSS-like query
     * @return this builder
     * @see the query syntax documentation in <a
     *      href="http://jsoup.org/apidocs/org/jsoup/select/Selector.html"
     *      >Selector</a>
     */
    public SiteMapBuilder last(String cssQuery) {
	siteMap.setEndOfDataCondition(cssQuery);
	return this;
    }

    /**
     * Sets the Selector CSS query that should match the links to detailed event
     * pages ( <code><a></code> elements) or the start elements where events'
     * details can be parsed.
     * 
     * @param cssQuery
     *            a Selector CSS-like query
     * @return this builder
     * @see the query syntax documentation in <a
     *      href="http://jsoup.org/apidocs/org/jsoup/select/Selector.html"
     *      >Selector</a>
     */
    public SiteMapBuilder li(String cssQuery) {
	siteMap.setListItemSelector(cssQuery);
	return this;
    }

    /**
     * TODO: not yet implemented.
     * 
     * @param num
     * @return
     */
    public SiteMapBuilder maxPages(int num) {
	siteMap.setMaxPages(num);
	return this;
    }

    /**
     * Returns the SiteMap object with the parameters set in this builder.
     * 
     * @return a SiteMap object.
     */
    public SiteMap build() {
	return siteMap;
    }

}
