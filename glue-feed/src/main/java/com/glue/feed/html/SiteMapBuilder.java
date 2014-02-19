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

    /**
     * Constructs a new builder for a SiteMap object. The given url is typically
     * the link to the first page of list items.
     * 
     * @param frontUrl
     */
    public SiteMapBuilder(String frontUrl) {
	this.siteMap = new SiteMap(frontUrl);
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
     * Either {@link #list(String)} or {@link #url(String)} or
     * {@link #url(URLFilter)} is required for pages listing multiple events.
     * Sets the Selector CSS query that should match the links to event details
     * in one page. The selector may directly include the link to the event
     * details page ( <code><a></code> element), or designate its unambiguous
     * ancestor. Otherwise, the selector designates the start element where a
     * bean can be mapped.
     * 
     * @param cssQuery
     *            a Selector CSS-like query
     * @return this builder
     * @see the query syntax documentation in <a
     *      href="http://jsoup.org/apidocs/org/jsoup/select/Selector.html"
     *      >Selector</a>
     */
    public SiteMapBuilder list(String cssQuery) {
	siteMap.setListSelector(cssQuery);
	return this;
    }

    /**
     * Sets the base URL with which all links to event details pages should
     * start with.
     * 
     * <p>
     * If the given URL is not absolute, it is resolved against the front URL.
     * </p>
     * 
     * @param baseUrl
     *            the base URL for all event details pages
     * @return this builder
     * @see #url(URLFilter)
     */
    public SiteMapBuilder url(String baseUrl) {
	url(new BaseURLFilter(baseUrl));
	return this;
    }

    /**
     * Sets the filter used to filter all the links to event details pages.
     * 
     * @param filter
     *            an URL filter
     * @return this builder
     * @see #url(String)
     */
    public SiteMapBuilder url(URLFilter filter) {
	siteMap.setUrlFilter(filter);
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
