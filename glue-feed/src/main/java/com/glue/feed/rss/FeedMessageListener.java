package com.glue.feed.rss;

public interface FeedMessageListener {
	
	void newMessage(FeedMessage msg) throws Exception;

}
