package com.glue.feed.rss;

import java.util.List;
import java.util.Set;

import com.glue.struct.IStream;
import com.glue.struct.IVenue;

public class ReadTest {
	public static void main(String[] args) {
		RSSFeedParser parser = new RSSFeedParser(
				"http://lebikini.com/programmation/rss");

		BikiniMessageListener listener = new BikiniMessageListener();

		parser.setFeedMessageListener(listener);
		parser.readFeed();

		List<IStream> streams = listener.getStreams();
		for (IStream stream : streams) {
			System.out.println(stream);
		}

		Set<IVenue> venues = listener.getVenues();
		for (IVenue venue : venues) {
			System.out.println(venue);
		}

	}
}
