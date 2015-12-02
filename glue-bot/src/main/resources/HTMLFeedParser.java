package com.glue.feed.html;

import java.io.IOException;

import javax.xml.transform.ErrorListener;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;

/**
 * Annotation-based HTML to Object Mapper using JSoup Parser.
 * 
 * @author pgillet
 * 
 * @param <T>
 */
public class HTMLFeedParser<T> implements ErrorHandler, ErrorManager,
	FeedParser<T>, Extractor {

    static final Logger LOG = LoggerFactory.getLogger(HTMLFeedParser.class);

    private FeedMessageListener<T> feedMessageListener = new DefaultFeedMessageListener<T>();

    private ErrorDispatcher errorDispatcher = new ErrorDispatcher();

    private BrowsingStrategy browsingStrategy;
    private HTMLMappingStrategy<T> mappingStrategy;

    /**
     * Pass in the class Java bean that will contain the mapped data from the
     * HTML source. The HTML source is described within the given site map, and
     * is traversed according to a PaginatedListStrategy.
     * 
     * @param classModel
     * @param siteMap
     */
    public HTMLFeedParser(Class<T> classModel, SiteMap siteMap) {
	this.browsingStrategy = new PaginatedListStrategy(siteMap);
	this.browsingStrategy.setExtractor(this);

	this.mappingStrategy = new AnnotationMappingStrategy<T>(classModel);
    }

    public HTMLFeedParser(SiteMap siteMap,
	    HTMLMappingStrategy<T> mappingStrategy) {
	this.browsingStrategy = new PaginatedListStrategy(siteMap);
	this.browsingStrategy.setExtractor(this);

	this.mappingStrategy = mappingStrategy;
    }

    @Override
    public void read() throws Exception {
	browsingStrategy.browse();
    }

    /**
     * Callback method from the <{@link BrowsingStrategy}.
     */
    @Override
    public void process(Element e) throws Exception {
	LOG.info("Parsing event details page");

	try {
	    T obj = mappingStrategy.parse(e);
	    feedMessageListener.newMessage(obj);
	} catch (Exception ex) {
	    LOG.error(ex.getMessage(), ex);
	    errorDispatcher.fireErrorEvent(ErrorLevel.ERROR, ex.getMessage(),
		    ex, "html", -1);
	}
    }

    @Override
    public FeedMessageListener<T> getFeedMessageListener() {
	return feedMessageListener;
    }

    public void setFeedMessageListener(
	    FeedMessageListener<T> feedMessageListener) {
	this.feedMessageListener = feedMessageListener;
    }

    @Override
    public void close() throws IOException {
	flush();
	feedMessageListener.close();
    }

    @Override
    public void flush() throws IOException {
	errorDispatcher.flush();
    }

    @Override
    public ErrorListener[] getErrorListeners() {
	return errorDispatcher.getErrorListeners();
    }

    @Override
    public void addErrorListener(ErrorListener l) {
	errorDispatcher.addErrorListener(l);

    }

    @Override
    public void removeErrorListener(ErrorListener l) {
	errorDispatcher.removeErrorListener(l);
    }

    @Override
    public void fireErrorEvent(ErrorLevel lvl, String message, Throwable cause,
	    String source, int lineNumber) {
	errorDispatcher.fireErrorEvent(lvl, message, cause, source, lineNumber);
    }

}
