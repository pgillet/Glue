package com.glue.feed.html;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

/**
 * A decorator that adds shortcut methods to a Jsoup Element.
 * 
 * @author ubuntu
 *
 */
public class ElementDecorator {

    private Element element;
    private HtmlCompressor compressor = new HtmlCompressor();


    public ElementDecorator(Element element) {
	this.element = element;
    }

    public Element getElement() {
	return element;
    }

    public ElementDecorator selectFirst(String cssQuery) {

	ElementDecorator elem = null;

	if (cssQuery != null) {
	    Elements elems = element.select(cssQuery);
	    if (!elems.isEmpty()) {
		elem = new ElementDecorator(elems.first());
	    }
	}

	return elem;
    }

    /**
     * A shortcut to {@link Element#select(String)#text()}. Returns
     * <code>null</code> if the given CSS query is <code>null</code>.
     * 
     * @param cssQuery
     * @return
     */
    public String selectText(String cssQuery) {
	String text = null;
	if (cssQuery != null) {
	    text = StringUtils.trimToNull(element.select(cssQuery).text());
	}

	return text;
    }

    public String selectText(String cssQuery, String defaultValue) {
	String text = selectText(cssQuery);

	if (text == null) {
	    text = defaultValue;
	}

	return text;
    }

    public String selectHtml(String cssQuery) {
	String bodyHtml = StringUtils.trimToNull(element.select(cssQuery)
		.html());

	if (bodyHtml != null) {
	    // Clean
	    bodyHtml = cleanHtml(bodyHtml);
	}

	return bodyHtml;
    }

    public String selectHtml(String cssQuery, String defaultValue) {
	String bodyHtml = selectHtml(cssQuery);

	if (bodyHtml == null) {
	    bodyHtml = defaultValue;
	}

	return bodyHtml;
    }

    public Set<String> listLinks(URLFilter filter) {

	Set<String> l = new HashSet<>();

	// if(filter instanceof BaseURLFilter){
	// String selector = "a[href^="
	// + ((BaseURLFilter) filter).getBaseUrl() + "]";
	// }

	Elements links = element.select("a[href]");
	for (Element link : links) {
	    String linkHref = link.attr("abs:href");
	    if (filter == null || filter.accept(linkHref)) {
		l.add(linkHref);
	    }
	}

	return l;
    }

    public Set<String> listLinks() {
	return listLinks(null);
    }

    public String firstLink(URLFilter filter) {

	// Set<String> links = listLinks(rootElem, filter);
	// return links.isEmpty() ? null : links.get(0);

	Elements links = element.select("a[href]");
	for (Element link : links) {
	    String linkHref = link.attr("abs:href");
	    if (filter == null || filter.accept(linkHref)) {
		return linkHref;
	    }
	}

	return null;
    }

    public String firstLink() {
	return firstLink(null);
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

}
