package com.glue.feed.dvr;

import java.util.ArrayList;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Venue;

@DisallowConcurrentExecution
public class VenueReconciliationJob implements Job {

    static final Logger LOG = LoggerFactory
	    .getLogger(VenueReconciliationJob.class);

    @Override
    public void execute(JobExecutionContext context)
	    throws JobExecutionException {

	try (VenueService service = new VenueServiceImpl()) {
	    List<Venue> venues = service.getUnresolvedVenues(500);

	    List<Venue> nomatches = new ArrayList<>();

	    for (Venue venue : venues) {

		LOG.info("Search for a reference venue for the venue = "
			+ venue);
		Venue venueRef = service.resolve(venue);
		LOG.info("Found reference venue = " + venueRef);

		if (venueRef == null) {
		    nomatches.add(venue);
		}

	    }

	    // TODO: should send an email to notify that some venues have no
	    // reference venue
	    if (!nomatches.isEmpty()) {
		for (Venue venue : nomatches) {
		    LOG.info("The following venue has no match = " + venue);
		}
	    }

	    LOG.info("Done");
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    throw new JobExecutionException(e);
	}

    }

    /**
     * @param args
     * @throws JobExecutionException
     */
    public static void main(String[] args) throws JobExecutionException {
	VenueReconciliationJob job = new VenueReconciliationJob();
	job.execute(null);
	System.exit(0);
    }

}
