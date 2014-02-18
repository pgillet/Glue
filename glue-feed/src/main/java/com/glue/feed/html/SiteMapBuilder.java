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
     * Constructs a new builder for a SiteMap object. The given uri is typically
     * the link to the first page of list items.
     * 
     * @param baseUri
     */
    public SiteMapBuilder(String baseUri) {
	this.siteMap = new SiteMap(baseUri);
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
     * Required for pages listing multiple events. Sets the Selector CSS query
     * that should match the list items in one page. The selector may directly
     * include the <a> element with the link to the event details page, or
     * designate its unambiguous ancestor. Otherwise, the selector designates
     * the start element where a bean can be mapped.
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
