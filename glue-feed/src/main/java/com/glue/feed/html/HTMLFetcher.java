package com.glue.feed.html;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

public class HTMLFetcher {

    private static final HtmlCompressor compressor = new HtmlCompressor();

    public static final String USER_AGENT = "Gluebot";

    RobotRulesParser robotRulesParser = new RobotRulesParser();

    public Set<String> listLinks(Element rootElem, URLFilter filter) {

	Set<String> l = new HashSet<>();

	// if(filter instanceof BaseURLFilter){
	// String selector = "a[href^="
	// + ((BaseURLFilter) filter).getBaseUrl() + "]";
	// }

	Elements links = rootElem.select("a[href]");
	for (Element link : links) {
	    String linkHref = link.attr("abs:href");
	    if (filter == null || filter.accept(linkHref)) {
		l.add(linkHref);
	    }
	}

	return l;
    }

    public Set<String> listLinks(Element rootElem) {
	return listLinks(rootElem, null);
    }

    public String firstLink(Element rootElem, URLFilter filter) {

	// Set<String> links = listLinks(rootElem, filter);
	// return links.isEmpty() ? null : links.get(0);

	Elements links = rootElem.select("a[href]");
	for (Element link : links) {
	    String linkHref = link.attr("abs:href");
	    if (filter == null || filter.accept(linkHref)) {
		return linkHref;
	    }
	}

	return null;
    }

    public String firstLink(Element rootElem) {
	return firstLink(rootElem, null);
    }

    public String selectText(String query, Element root) {
	return StringUtils.trimToNull(root.select(query).text());
    }

    public String selectHtml(String query, Element root) {
	String bodyHtml = StringUtils.trimToNull(root.select(query).html());

	if (bodyHtml != null) {
	    // Clean
	    bodyHtml = cleanHtml(bodyHtml);
	}

	return bodyHtml;
    }

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
