package com.glue.feed.fnac.venue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Venue;
import com.glue.feed.FeedMessageListener;
import com.glue.feed.GlueObjectBuilder;
import com.glue.feed.error.StoreErrorListener;
import com.glue.feed.io.FileExtensionFilter;
import com.glue.feed.io.GlueIOUtils;
import com.glue.feed.listener.VenueMessageListener;
import com.glue.feed.xml.XMLFeedParser;

public class FnacVenueJob implements Job {

    static final Logger LOG = LoggerFactory.getLogger(FnacVenueJob.class);

    // No arg constructor
    public FnacVenueJob() {
    }

    @Override
    public void execute(JobExecutionContext context)
	    throws JobExecutionException {

	try {
	    URL url = new URL(
		    "http://www.francebillet.com/static/uploads/Newspart/zip/lieu_20140411.zip");

	    ZipInputStream zin = new ZipInputStream(url.openStream());
	    ZipEntry entry = GlueIOUtils.getEntry(zin, new FileExtensionFilter(
		    ".xml"));
	    InputStream in = GlueIOUtils.getDeferredInputStream(zin,
		    entry.getName());

	    BufferedReader reader = new BufferedReader(new InputStreamReader(
		    in, Charset.forName("ISO-8859-1")));

	    XMLFeedParser<Lieu> parser = new XMLFeedParser<Lieu>(reader,
		    Lieu.class);

	    final FeedMessageListener<Venue> delegate = new VenueMessageListener();
	    final GlueObjectBuilder<Lieu, Venue> venueBuilder = new FnacVenueBeanBuilder();

	    parser.setFeedMessageListener(new FeedMessageListener<Lieu>() {

		@Override
		public void newMessage(Lieu msg) throws Exception {
		    delegate.newMessage(venueBuilder.build(msg));
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
	FnacVenueJob job = new FnacVenueJob();
	job.execute(null);
    }

}
