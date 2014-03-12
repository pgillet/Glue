package com.glue.feed.toulouse.bikini;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.feed.FeedMessageListener;
import com.glue.feed.GlueObjectBuilder;
import com.glue.feed.error.StoreErrorListener;
import com.glue.feed.io.GlueIOUtils;
import com.glue.feed.listener.StreamMessageListener;
import com.glue.feed.xml.XMLFeedParser;

public class BikiniJob implements Job {

	static final Logger LOG = LoggerFactory.getLogger(BikiniJob.class);

	// No arg constructor
	public BikiniJob() {
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		try {
			URL url = new URL("http://lebikini.com/programmation/rss");
			InputStream in0 = url.openStream();
			InputStream in = GlueIOUtils.getDeferredInputStream(in0,
					url.getFile());

			Reader reader0 = new InputStreamReader(in, "UTF-8");
			Reader reader = new BufferedReader(reader0);

			XMLFeedParser<Item> parser = new XMLFeedParser<Item>(reader,
					Item.class);

			final FeedMessageListener delegate = new StreamMessageListener();
			final GlueObjectBuilder<Item, Event> eventBuilder = new ItemStreamBuilder();

			parser.setFeedMessageListener(new FeedMessageListener<Item>() {

				@Override
				public void newMessage(Item msg) throws Exception {
					Event event = eventBuilder.build(msg);
					delegate.newMessage(event);
				}

				@Override
				public void close() throws IOException {
					delegate.close();
				}
			});

			parser.addErrorListener(new StoreErrorListener());

			parser.read();
			parser.close();
			parser.flush();
			LOG.info("Done");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new JobExecutionException(e);
		}
	}

	public static void main(String[] args) throws JobExecutionException {
		BikiniJob job = new BikiniJob();
		job.execute(null);
	}
}
