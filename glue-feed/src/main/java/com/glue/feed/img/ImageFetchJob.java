package com.glue.feed.img;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;

public class ImageFetchJob implements Job {

    static final Logger LOG = LoggerFactory.getLogger(ImageFetchJob.class);

    @Override
    public void execute(JobExecutionContext context)
	    throws JobExecutionException {
	ImageService service = new ImageServiceImpl();

	try {
	    
	    Date dateRef = null;
	    if(context != null){
		dateRef = context.getPreviousFireTime(); // May be null
	    }
	    if (dateRef == null) {
		dateRef = getYesterdayDate();
	    }

	    List<Event> events = service.getEventsCreatedAfter(dateRef);

	    ImageFetcher imageFetcher = new ImageFetcher();

	    for (Event event : events) {
		System.out.println("event = " + event.getTitle());
		try {
		    imageFetcher.fetchEventImage(event);
		} catch (IOException e) {
		    LOG.warn(e.getMessage());
		}
	    }

	    LOG.info("Done");

	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    throw new JobExecutionException(e);
	}

    }

    public Date getYesterdayDate() {
	// Yesterday
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DATE, -1);
	cal.set(Calendar.HOUR, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);

	return cal.getTime();
    }

    public static void main(String[] args) throws JobExecutionException {
	Job job = new ImageFetchJob();
	job.execute(null);
    }

}
