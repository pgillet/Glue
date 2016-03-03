package com.glue.bot;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.catalog.domain.SiteMap;

public class PaginatedListStrategy implements BrowsingStrategy {

    static final Logger LOG = LoggerFactory
	    .getLogger(PaginatedListStrategy.class);

    private SiteMap siteMap;

    private Extractor extractor = new DefaultExtractor();

    private HtmlFetcher hf = new HtmlFetcher();

    public PaginatedListStrategy(SiteMap siteMap) {
	this.siteMap = siteMap;
    }

    @Override
    public void browse() throws Exception {
	String baseUrl = siteMap.getFrontUrl();
	Document doc = hf.fetch(baseUrl);

	boolean nextPage = false;
	int numPage = 0;

	do {
	    numPage++;
	    LOG.info("Page number  = " + numPage);

	    // Elements that match the list selector
	    Elements elems = doc.select(siteMap.getListItemSelector());
	    Validate.notEmpty(elems);

	    for (Element e : elems) {
		extractor.process(e);
	    }

	    // Pagination
	    // Elements that match the end of data condition
	    if (siteMap.getEndOfDataCondition() != null) {
		Elements lastPageElems = doc.select(siteMap
			.getEndOfDataCondition());
		if (!lastPageElems.isEmpty()) {
		    // We reached the last page
		    break;
		}
	    }

	    // Elements that match the next page selector
	    if (siteMap.getNextPageSelector() != null) {
		Elements nextPageElems = doc.select(siteMap
			.getNextPageSelector());

		nextPage = !nextPageElems.isEmpty();

		// The selector must be sufficiently selective so as to select
		// only one element
		Validate.oneAtMost(nextPageElems);

		if (nextPage) {
		    // Load the next page
		    nextPageElems = nextPageElems.select(HtmlTags.LINK);
		    Validate.oneAtMost(nextPageElems);

		    String nextPageUrl = nextPageElems.attr("abs:href");
		    nextPage = StringUtils.isNotBlank(nextPageUrl);

		    if (nextPage) {
			doc = hf.fetch(nextPageUrl);
		    }
		}
	    }

	} while (nextPage);

    }

    public Extractor getExtractor() {
	return extractor;
    }

    @Override
    public void setExtractor(Extractor visitorListener) {
	this.extractor = visitorListener;
    }

}
