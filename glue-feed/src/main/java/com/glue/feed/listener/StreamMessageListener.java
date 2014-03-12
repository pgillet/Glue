package com.glue.feed.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.Venue;
import com.glue.feed.FeedMessageListener;
import com.glue.persistence.GluePersistenceService;

public class StreamMessageListener extends GluePersistenceService implements
	FeedMessageListener<Event> {

    static final Logger LOG = LoggerFactory
	    .getLogger(StreamMessageListener.class);

    public StreamMessageListener() {
	super();
    }

    @Override
    public void newMessage(final Event event) throws Exception {

	try {
	    // Begin transaction
	    begin();

	    Venue venue = event.getVenue();
	    if (venue == null) {
		LOG.trace("Events without a venue are not allowed");
		return;
	    }

	    // Search for an existing venue
	    Venue persistentVenue = getVenueDAO().findDuplicate(venue);
	    if (persistentVenue == null) {
		LOG.info("Inserting " + venue);
		persistentVenue = getVenueDAO().create(venue);
	    } else {
		LOG.info("Venue already exists = " + venue);
	    }
	    event.setVenue(persistentVenue);

	    if (!getEventDAO().hasDuplicate(event)) {
		LOG.info("Inserting " + event);
		getEventDAO().update(event);

	    } else {
		LOG.info("Event already exists = " + event);
	    }

	    // End transaction
	    commit();
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    rollback();
	    throw e;
	} finally {

	}
    }
}
