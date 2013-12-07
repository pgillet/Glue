package com.glue.feed;

import java.io.Closeable;

public interface FeedMessageListener<T> extends Closeable {
	
	void newMessage(T msg) throws Exception;

}
