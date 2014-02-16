package com.glue.feed.html;

import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaginatedListStrategy implements VisitorStrategy {

    static final Logger LOG = LoggerFactory
	    .getLogger(PaginatedListStrategy.class);

    private SiteMap siteMap;

    public PaginatedListStrategy(SiteMap siteMap) {
	this.siteMap = siteMap;
    }

    @Override
    public void visit() throws IOException {
	String baseUri = siteMap.getBaseUri();
	Document doc = Jsoup.connect(baseUri).get();

	boolean nextPage = false;
	int numPage = 0;

	do {
	    numPage++;
	    LOG.info("Page number  = " + numPage);

	    // Elements that match the list selector
	    Elements list = doc.select(siteMap.getListSelector());
	    if (list.isEmpty()) {
		// One of the end of data conditions
		// TODO: should be composable!
		break;
	    }

	    Iterator<Element> iter = list.iterator();
	    while (iter.hasNext()) {
		Element elem = iter.next();

		if (!"a".equals(elem.tagName())) {
		    // Find the first child <a> element that descend from given
		    // ancestor
		    elem = elem.select("a").first();
		}
		if (elem != null) {
		    String linkHref = elem.attr("abs:href");
		    LOG.info("Item link = " + linkHref);
		}
	    }

	    // Pagination
	    // Elements that match the end of data condition
	    if (siteMap.getEndOfDataCondition() != null) {
		Elements lastElems = doc
			.select(siteMap.getEndOfDataCondition());
		if (!lastElems.isEmpty()) {
		    // We reached the last page
		    break;
		}
	    }

	    // Elements that match the next page selector
	    if (siteMap.getNextPageSelector() != null) {
		Elements pageElems = doc.select(siteMap.getNextPageSelector());
		Iterator<Element> pages = pageElems.iterator();

		nextPage = pages.hasNext();

		if (nextPage) {
		    // Load the next page
		    Element pageElem = pages.next();
		    Element link = pageElem.select("a").first();
		    if (link != null) {
			String linkHref = link.attr("abs:href");
			doc = Jsoup.connect(linkHref).get();
		    }
		}
	    }

	} while (nextPage);

    }

}
