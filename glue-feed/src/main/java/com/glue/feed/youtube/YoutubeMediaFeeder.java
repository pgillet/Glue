package com.glue.feed.youtube;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.persistence.GluePersistenceService;

public class YoutubeMediaFeeder extends GluePersistenceService implements Job {

    static final Logger LOG = LoggerFactory.getLogger(YoutubeMediaFeeder.class);

    private Calendar calendar;

    public YoutubeMediaFeeder() {
	super();

	TimeZone tz = TimeZone.getTimeZone("UTC");
	calendar = Calendar.getInstance(tz);
	// reset hour, minutes, seconds and millis
	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	calendar.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public void execute(JobExecutionContext context)
	    throws JobExecutionException {
	Date end = calendar.getTime();
	calendar.add(Calendar.DATE, -7);
	Date start = calendar.getTime();

	try {
	    // Begin transaction
	    begin();

	    // Search for streams from "after" to "today"
	    List<Event> events = getEventDAO().findBetween(start, end);

	    // For each streams from "after" to "today"
	    for (Event event : events) {

		LOG.info("Event " + event.getTitle());

		// Search for videos for that stream
		event = YoutubeRequester.getInstance().search(event);

		getEventDAO().update(event);
	    }

	    // End transaction
	    commit();
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    rollback();
	    throw new JobExecutionException(e);
	} finally {

	}
    }

    public static void main(String[] args) throws JobExecutionException {
	YoutubeMediaFeeder job = new YoutubeMediaFeeder();
	job.execute(null);
    }
}
