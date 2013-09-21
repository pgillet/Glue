package com.glue.feed.rss;

import java.sql.SQLException;

import javax.naming.NamingException;

public class ReadTest {
	public static void main(String[] args) throws NamingException, SQLException {
		RSSFeedParser parser = new RSSFeedParser(
				"http://lebikini.com/programmation/rss");

		BikiniMessageListener listener = new BikiniMessageListener();

		parser.setFeedMessageListener(listener);
		parser.readFeed();
	}
}
