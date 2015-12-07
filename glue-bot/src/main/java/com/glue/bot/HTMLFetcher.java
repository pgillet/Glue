package com.glue.bot;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

public class HTMLFetcher {

    static final Logger LOG = LoggerFactory.getLogger(HTMLFetcher.class);

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


    /**
     * If the given element is a hyperlink containing the URI of a resource to
     * go to, then navigates seamlessly to this resource and returns the Element
     * for this resource. Returns the given Element otherwise.
     * 
     * @param elem
     * @return
     * @throws IOException
     */
    public Element navigateTo(Element elem) throws IOException {

	Element result = elem;

	if ("a".equals(elem.tagName())) {
	    String url = elem.attr("abs:href");
	    result = fetch(url);
	}

	return result;
    }

    public Document fetch(String url) throws IOException {

	LOG.info("Fetching HTML document = " + url);

	if (!robotRulesParser.isAllowed(url)) {
	    throw new IOException(url + " not allowed by robots.txt");
	}

	return Jsoup.connect(url).userAgent(USER_AGENT).get();
    }

}
