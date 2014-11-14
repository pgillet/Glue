package com.glue.feed.html;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.FeedMessageListener;
import com.glue.feed.FeedParser;
import com.glue.feed.error.ErrorDispatcher;
import com.glue.feed.error.ErrorHandler;
import com.glue.feed.error.ErrorLevel;
import com.glue.feed.error.ErrorListener;
import com.glue.feed.error.ErrorManager;
import com.glue.feed.listener.DefaultFeedMessageListener;

/**
 * Annotation-based HTML to Object Mapper using JSoup Parser.
 * 
 * @author pgillet
 * 
 * @param <T>
 */
public class HTMLFeedParser<T> implements ErrorHandler, ErrorManager,
	FeedParser<T>, VisitorListener {

    static final Logger LOG = LoggerFactory.getLogger(HTMLFeedParser.class);

    private FeedMessageListener<T> feedMessageListener = new DefaultFeedMessageListener<T>();

    private ErrorDispatcher errorDispatcher = new ErrorDispatcher();

    private VisitorStrategy visitorStrategy;
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
	this.visitorStrategy = new PaginatedListStrategy(siteMap);
	this.visitorStrategy.setVisitorListener(this);

	this.mappingStrategy = new AnnotationMappingStrategy<T>(classModel);
    }

    public HTMLFeedParser(SiteMap siteMap,
	    HTMLMappingStrategy<T> mappingStrategy) {
	this.visitorStrategy = new PaginatedListStrategy(siteMap);
	this.visitorStrategy.setVisitorListener(this);

	this.mappingStrategy = mappingStrategy;
    }

    @Override
    public void read() throws Exception {
	visitorStrategy.visit();
    }

    @Override
    public void processLink(String uri) throws Exception {
	LOG.info("Parsing event details page = " + uri);

	try {
	    T obj = mappingStrategy.parse(uri);
	    feedMessageListener.newMessage(obj);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    errorDispatcher.fireErrorEvent(ErrorLevel.ERROR, e.getMessage(), e,
		    "html", -1);
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
