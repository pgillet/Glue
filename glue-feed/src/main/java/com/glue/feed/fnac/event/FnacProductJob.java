package com.glue.feed.fnac.event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;

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

public class FnacProductJob implements Job {

    static final Logger LOG = LoggerFactory.getLogger(FnacProductJob.class);

    // No arg constructor
    public FnacProductJob() {
    }

    @Override
    public void execute(JobExecutionContext context)
	    throws JobExecutionException {

	try {
	    URL url = new URL(
		    "http://productdata.zanox.com/exportservice/v1/rest/27913013C35305384.xml?ticket=60B7DD55ECBEDCFC02E1343BBC4CA087&gZipCompress=true");

	    GZIPInputStream zin = new GZIPInputStream(url.openStream());

	    InputStream in = GlueIOUtils.getDeferredInputStream(zin, "tmp.gz");

	    BufferedReader reader = new BufferedReader(new InputStreamReader(
		    in, "UTF-8"));

	    XMLFeedParser<Product> parser = new XMLFeedParser<Product>(reader,
		    Product.class);

	    final FeedMessageListener<Event> delegate = new StreamMessageListener();
	    final GlueObjectBuilder<Product, Event> builder = new FnacProductBuilder();

	    parser.setFeedMessageListener(new FeedMessageListener<Product>() {

		@Override
		public void newMessage(Product msg) throws Exception {
		    delegate.newMessage(builder.build(msg));
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
	FnacProductJob job = new FnacProductJob();
	job.execute(null);
	System.exit(0);
    }

}
