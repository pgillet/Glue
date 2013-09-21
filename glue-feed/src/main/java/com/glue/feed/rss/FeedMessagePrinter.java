package com.glue.feed.rss;

/**
 * A simple listener that prints the messages on the standard output.
 * @author pgillet
 *
 */
public class FeedMessagePrinter implements FeedMessageListener {

	@Override
	public void newMessage(FeedMessage msg) {
		System.out.println(msg);
	}

	@Override
	public void close() {
	}

}
