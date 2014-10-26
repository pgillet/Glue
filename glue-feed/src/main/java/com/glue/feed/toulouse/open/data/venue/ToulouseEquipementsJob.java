package com.glue.feed.toulouse.open.data.venue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.input.BOMInputStream;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Venue;
import com.glue.feed.FeedMessageListener;
import com.glue.feed.GlueObjectBuilder;
import com.glue.feed.csv.CSVFeedParser;
import com.glue.feed.io.FileExtensionFilter;
import com.glue.feed.io.GlueIOUtils;
import com.glue.feed.listener.VenueMessageListener;

/**
 * Read from a CSV file the list of cultural equipement in Toulouse and
 * surroundings.
 * 
 * @author grdenis
 */
@DisallowConcurrentExecution
public class ToulouseEquipementsJob implements Job {

    static final Logger LOG = LoggerFactory
	    .getLogger(ToulouseEquipementsJob.class);

    // No arg constructor
    public ToulouseEquipementsJob() {
    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

	try {
	    URL url = new URL(
		    "http://data.grandtoulouse.fr/web/guest/les-donnees/-/opendata/card/23851-equipements-culturels/resource/document?p_p_state=exclusive&_5_WAR_opendataportlet_jspPage=%2Fsearch%2Fview_card_license.jsp");
	    ZipInputStream zin = new ZipInputStream(url.openStream());
	    ZipEntry entry = GlueIOUtils.getEntry(zin, new FileExtensionFilter(
		    ".csv"));
	    InputStream in = GlueIOUtils.getDeferredInputStream(zin,
		    entry.getName());

	    BufferedReader reader = new BufferedReader(new InputStreamReader(
		    new BOMInputStream(in), Charset.forName("UTF-8")));
	    CSVFeedParser<VenueBean> parser = new CSVFeedParser<>(reader,
		    VenueBean.class);

	    final FeedMessageListener<Venue> delegate = new VenueMessageListener();
	    final GlueObjectBuilder<VenueBean, Venue> venueBuilder = new VenueBeanVenueBuilder();

	    parser.setFeedMessageListener(new FeedMessageListener<VenueBean>() {

		@Override
		public void newMessage(VenueBean msg) throws Exception {
		    Venue venue = venueBuilder.build(msg);
		    delegate.newMessage(venue);
		}

		@Override
		public void close() throws IOException {
		    delegate.close();
		}
	    });

	    parser.read();
	    parser.close();
	    LOG.info("Done");
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    throw new JobExecutionException(e);
	}
    }

    public static void main(String[] args) throws JobExecutionException {
	ToulouseEquipementsJob job = new ToulouseEquipementsJob();
	job.execute(null);
	System.exit(0);
    }
}
