package com.glue.feed.html;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

public class HTMLFetcher {

    private static final HtmlCompressor compressor = new HtmlCompressor();

    public static final String USER_AGENT = "Gluebot";

    RobotRulesParser robotRulesParser = new RobotRulesParser();

    /**
     * Sanitizes HTML from input HTML, by parsing input HTML and filtering it
     * through a basic white-list of permitted tags and attributes.
     * Additionally, the resulting HTML is minified.
     */
    public String cleanHtml(String str) {
	String bodyHtml = Jsoup.clean(str, Whitelist.basic());

	// Then compress
	compressor.setRemoveIntertagSpaces(true);
	bodyHtml = compressor.compress(bodyHtml);

	return bodyHtml;
    }

    public Document fetch(String url) throws IOException {

	if (!robotRulesParser.isAllowed(url)) {
	    throw new IOException(url + " not allowed by robots.txt");
	}

	return Jsoup.connect(url).userAgent(USER_AGENT).get();
    }

}
