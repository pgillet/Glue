package com.glue.feed.xml;

public interface FeedMessageListener<T> {
	
	void newMessage(T msg) throws Exception;
	
	/**
	 * Closes this listener and releases any system resources associated with it. 
	 */
	void close();

}
