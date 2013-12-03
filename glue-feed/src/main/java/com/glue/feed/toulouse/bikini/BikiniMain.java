package com.glue.feed.toulouse.bikini;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.xml.stream.StreamFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.xml.ElementFilter;
import com.glue.feed.xml.FeedMessageListener;
import com.glue.feed.xml.StreamBuilder;
import com.glue.feed.xml.StreamMessageListener;
import com.glue.feed.xml.XMLFeedParser;
import com.glue.struct.IStream;

public class BikiniMain {

	static final Logger LOG = LoggerFactory.getLogger(BikiniMain.class);

	public static void main(String[] args) throws Exception {

		URL url = new URL("http://lebikini.com/programmation/rss");
		InputStream in = url.openStream();

		Reader reader0 = new InputStreamReader(in, "UTF-8");
		Reader reader = new BufferedReader(reader0);

		StreamFilter filter = new ElementFilter("item");

		XMLFeedParser<Item> parser = new XMLFeedParser<Item>(reader, filter,
				Item.class);

		final FeedMessageListener delegate = new StreamMessageListener();
		final StreamBuilder streamBuilder = new ItemStreamBuilder();

		parser.setFeedMessageListener(new FeedMessageListener<Item>() {

			@Override
			public void newMessage(Item msg) throws Exception {
				IStream stream = streamBuilder.buildStream(msg);
				delegate.newMessage(stream);
			}

			@Override
			public void close() {
				delegate.close();
			}
		});

		parser.read();
		delegate.close();
		LOG.info("Done");

	}
}
