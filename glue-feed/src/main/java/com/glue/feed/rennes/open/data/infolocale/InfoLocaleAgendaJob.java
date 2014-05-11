package com.glue.feed.rennes.open.data.infolocale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

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
import com.glue.feed.listener.StreamMessageListener;

/**
 * Read from a CSV file the list of cultural events in Rennes and surroundings.
 * 
 * @author grdenis
 */
@DisallowConcurrentExecution
public class InfoLocaleAgendaJob implements Job {

    static final Logger LOG = LoggerFactory
	    .getLogger(InfoLocaleAgendaJob.class);

    // No arg constructor
    public InfoLocaleAgendaJob() {
    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

	try {
	    URL url = new URL(
		    "http://data.infolocale.fr/explore/dataset/agenda_culturel/download?format=csv");

	    InputStream in = url.openStream();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(
		    in, Charset.forName("UTF-8")));

	    CSVFeedParser<InfoLocaleBean> parser = new CSVFeedParser<>(reader,
		    InfoLocaleBean.class);

	    final FeedMessageListener<Event> delegate = new StreamMessageListener();
	    final GlueObjectBuilder<InfoLocaleBean, Event> eventBuilder = new InfoLocaleBeanStreamBuilder();

	    parser.setFeedMessageListener(new FeedMessageListener<InfoLocaleBean>() {

		@Override
		public void newMessage(InfoLocaleBean msg) throws Exception {
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
	InfoLocaleAgendaJob job = new InfoLocaleAgendaJob();
	job.execute(null);
    }
}
