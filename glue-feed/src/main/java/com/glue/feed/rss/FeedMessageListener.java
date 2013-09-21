package com.glue.feed.rss;

public interface FeedMessageListener {
	
	void newMessage(FeedMessage msg) throws Exception;
	
	/**
	 * Closes this listener and releases any system resources associated with it. 
	 */
	void close();

}
