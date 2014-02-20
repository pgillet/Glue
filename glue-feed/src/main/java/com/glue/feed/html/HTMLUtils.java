package com.glue.feed.html;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLUtils {

    public static Set<String> listLinks(Element rootElem, URLFilter filter) {

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

    public static Set<String> listLinks(Element rootElem) {
	return listLinks(rootElem, null);
    }

    public static String firstLink(Element rootElem, URLFilter filter) {

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

    public static String firstLink(Element rootElem) {
	return firstLink(rootElem, null);
    }

    public static String selectText(String query, Element root) {
	return StringUtils.trimToNull(root.select(query).text());
    }

    public static String selectHtml(String query, Element root) {
	return StringUtils.trimToNull(root.select(query).html());
    }

}
