package com.glue.bot;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
public class HtmlFragment {

    private Element element;
    private HtmlCompressor compressor = new HtmlCompressor();

    public HtmlFragment(Element element) {
	this.element = element;
    }

    public Element getElement() {
	return element;
    }

    /**
     * Return {@link Element#select(String)} as a List of HtmlElement.
     * 
     * @param cssQuery
     * @return
     */
    public List<HtmlFragment> select(String cssQuery) {
	return element.select(cssQuery).stream()
		.map(e -> HtmlFragment.valueOf(e)).collect(Collectors.toList());
    }

    /**
     * Find elements that match the Selector CSS query as
     * {@link Element#select(String)}, but throw an IllegalStateException if
     * none match.
     * 
     * @param cssQuery
     * @return elements that match the query
     * @throws SelectorParseException
     *             if no elements match the query
     */
    public List<HtmlFragment> selectNotEmpty(String cssQuery)
	    throws SelectorParseException {
	Elements elements = element.select(cssQuery);

	if (elements.isEmpty()) {
	    throw new SelectorParseException("No elements found", cssQuery);
	}

	return elements.stream().map(e -> HtmlFragment.valueOf(e))
		.collect(Collectors.toList());
    }

    /**
     * Find the single element that matches the Selector CSS query as
     * {@link Element#select(String)}, but throw an SelectorParseException if
     * more than one element matches.
     * 
     * <p>
     * This means that the selector must be sufficiently selective so as to
     * select only one element.
     * </p>
     * 
     * @param cssQuery
     * @return
     * @throws SelectorParseException
     */
    public HtmlFragment selectSingle(String cssQuery)
	    throws SelectorParseException {
	List<HtmlFragment> elements = selectNotEmpty(cssQuery);

	if (elements.size() > 1) {
	    throw new SelectorParseException("More than one element found",
		    cssQuery);
	}

	return elements.get(0);
    }

    /**
     * Test if this element is a link element, with the <a> tag.
     * 
     * @return true if <a>, false otherwise
     */
    public boolean isLink() {
	return "a".equals(element.tagName());
    }

    /**
     * Find the first element that matches the Selector CSS query, with this
     * element as the starting context.
     * 
     * @param cssQuery
     * @return
     */
    public HtmlFragment selectFirst(String cssQuery) {

	HtmlFragment elem = null;

	if (cssQuery != null) {
	    Elements elems = element.select(cssQuery);
	    if (!elems.isEmpty()) {
		elem = new HtmlFragment(elems.first());
	    }
	}

	return elem;
    }

    /**
     * A shortcut to {@link Element#select(String)#text()}. Returns
     * <code>null</code> if no element matches the query.
     * 
     * @param cssQuery
     * @return
     */
    public String selectText(String cssQuery) {
	return StringUtils.trimToNull(element.select(cssQuery).text());
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

    public List<Link> listLinks() {
	return listLinks(null);
    }

    public List<Link> listLinks(Predicate<String> urlFilter) {

	// if(filter instanceof BaseURLFilter){
	// String selector = "a[href^="
	// + ((BaseURLFilter) filter).getBaseUrl() + "]";
	// }

	List<Link> links = stream(urlFilter).collect(Collectors.toList());

	return links;
    }

    /**
     * Returns a stream over the given link Elements that match the URL filter.
     * 
     * @param urlFilter
     * @param elems
     * @return
     */
    private Stream<Link> stream(Predicate<String> urlFilter) {
	Elements elems = element.select("a[href]");

	return elems
		.stream()
		.filter(elem -> urlFilter == null
			|| urlFilter.test(elem.attr("abs:href")))
		.map(elem -> new Link(elem.attr("abs:href"), StringUtils
			.trimToNull(elem.text()))).distinct();
    }

    public Link firstLink(Predicate<String> urlFilter) {

	Link link = stream(urlFilter).findFirst().get();

	return link;
    }

    public Link firstLink() {
	return firstLink(null);
    }

    public Link singleLink(Predicate<String> urlFilter)
	    throws SelectorParseException {
	List<Link> links = listLinks(urlFilter);

	if (links.isEmpty()) {
	    throw new SelectorParseException("No links found");
	}
	if (links.size() > 1) {
	    throw new SelectorParseException("More than one link found");
	}

	return links.get(0);
    }

    public Link singleLink() throws SelectorParseException {
	return singleLink(null);
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

    public static HtmlFragment valueOf(Element e) {
	return new HtmlFragment(e);
    }

    public static void main(String[] args) throws IOException,
	    SelectorParseException {
	// Validate.isTrue(args.length == 1, "usage: supply url to fetch");
	// String url = args[0];

	String url = "http://www.lebikini.com/programmation/index/date/new";

	print("Fetching %s...", url);

	Document doc = Jsoup.connect(url).get();
	HtmlFragment elem = new HtmlFragment(doc);

	List<Link> links = elem.listLinks(s -> s
		.startsWith("http://www.lebikini.com/programmation/concert/"));
	Elements media = doc.select("[src]");
	Elements imports = doc.select("link[href]");

	// print("\nMedia: (%d)", media.size());
	// for (Element src : media) {
	// if (src.tagName().equals("img"))
	// print(" * %s: <%s> %sx%s (%s)",
	// src.tagName(), src.attr("abs:src"), src.attr("width"),
	// src.attr("height"),
	// trim(src.attr("alt"), 20));
	// else
	// print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
	// }
	//
	// print("\nImports: (%d)", imports.size());
	// for (Element link : imports) {
	// print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"),
	// link.attr("rel"));
	// }

	print("\nLinks: (%d)", links.size());
	for (Link link : links) {
	    print(" * a: <%s>  (%s)", link.getName(), link.getUrl());
	}

	print("\nFirst Link");
	Link link = elem.firstLink();
	print(" * a: <%s>  (%s)", link.getName(), link.getUrl());

	String html = "<html><head><title>First parse</title></head>"
		+ "<body><p>Parsed HTML into a <a href=\"http://www.example.com\">doc</a></p></body></html>";
	doc = Jsoup.parse(html);

	elem = new HtmlFragment(doc);

	elem = elem.selectSingle("a[href]");

	System.out.println("Is link = " + elem.isLink());
    }

    private static void print(String msg, Object... args) {
	System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
	if (s.length() > width)
	    return s.substring(0, width - 1) + ".";
	else
	    return s;
    }

}
