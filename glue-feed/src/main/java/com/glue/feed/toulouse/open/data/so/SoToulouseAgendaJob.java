package com.glue.feed.toulouse.open.data.so;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.feed.FeedMessageListener;
import com.glue.feed.GlueObjectBuilder;
import com.glue.feed.csv.CSVFeedParser;
import com.glue.feed.error.StoreErrorListener;
import com.glue.feed.io.FileExtensionFilter;
import com.glue.feed.io.GlueIOUtils;
import com.glue.feed.listener.StreamMessageListener;

/**
 * Read from a CSV file the list of cultural events in Toulouse and
 * surroundings.
 * 
 * @author grdenis
 */
@DisallowConcurrentExecution
public class SoToulouseAgendaJob implements Job {

    static final Logger LOG = LoggerFactory
	    .getLogger(SoToulouseAgendaJob.class);

    // No arg constructor
    public SoToulouseAgendaJob() {
    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

	try {
	    URL url = new URL(
		    "http://data.grandtoulouse.fr/web/guest/les-donnees/-/opendata/card/21905-agenda-des-manifestations-culturelles/resource/document?p_p_state=exclusive&_5_WAR_opendataportlet_jspPage=%2Fsearch%2Fview_card_license.jsp");

	    ZipInputStream zin = new ZipInputStream(url.openStream());
	    ZipEntry entry = GlueIOUtils.getEntry(zin, new FileExtensionFilter(
		    ".csv"));
	    InputStream in = GlueIOUtils.getDeferredInputStream(zin,
		    entry.getName());

	    // CSV files encoding = "Windows-1252"
	    BufferedReader reader = new BufferedReader(new InputStreamReader(
		    in, Charset.forName("Windows-1252")));

	    CSVFeedParser<EventBean> parser = new CSVFeedParser<>(reader,
		    EventBean.class);

	    final FeedMessageListener<Event> delegate = new StreamMessageListener();
	    final GlueObjectBuilder<EventBean, Event> eventBuilder = new EventBeanStreamBuilder();

	    parser.setFeedMessageListener(new FeedMessageListener<EventBean>() {

		@Override
		public void newMessage(EventBean msg) throws Exception {
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
	SoToulouseAgendaJob job = new SoToulouseAgendaJob();
	job.execute(null);
	System.exit(0);
    }
}
