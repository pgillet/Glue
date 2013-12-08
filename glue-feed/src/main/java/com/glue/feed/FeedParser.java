package com.glue.feed;

import java.io.Closeable;


public interface FeedParser<T> extends Closeable {

	FeedMessageListener<T> getFeedMessageListener();

	void read() throws Exception;

}
