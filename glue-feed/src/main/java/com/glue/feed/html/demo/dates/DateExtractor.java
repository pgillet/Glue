package com.glue.feed.html.demo.dates;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.glue.feed.html.EventDetailsPage;
import com.glue.feed.html.HTMLFetcher;
import com.glue.feed.html.HTMLMappingStrategy;

public class DateExtractor implements HTMLMappingStrategy<String> {

    private EventDetailsPage details;
    private HTMLFetcher hf = new HTMLFetcher();

    public DateExtractor(EventDetailsPage details)
	    throws IOException {
	this.details = details;
    }

    @Override
    public String parse(String url) throws Exception {
	Connection conn = Jsoup.connect(url);
	conn.timeout(0);

	Element doc = conn.get();

	if (details.getRootBlock() != null) {
	    Elements elems = doc.select(details.getRootBlock());
	    if (!elems.isEmpty()) {
		doc = elems.first();
	    }
	}

	String dateQuery = details.getStartDate();
	String text = hf.selectText(dateQuery, doc);

	return text;
    }

}
