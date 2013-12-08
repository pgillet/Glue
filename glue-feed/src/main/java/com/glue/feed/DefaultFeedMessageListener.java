package com.glue.feed;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.xml.XMLFeedParser;

/**
 * A simple listener that logs an INFO message.
 * 
 * @author pgillet
 * 
 * @param <T>
 */
public class DefaultFeedMessageListener<T> implements FeedMessageListener<T> {

	static final Logger LOG = LoggerFactory.getLogger(XMLFeedParser.class);

	@Override
	public void newMessage(T msg) throws Exception {
		LOG.info(msg.toString());
	}

	@Override
	public void close() throws IOException {
		// Does nothing
	}

}
