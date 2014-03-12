package com.glue.feed.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Venue;
import com.glue.feed.FeedMessageListener;
import com.glue.persistence.GluePersistenceService;

public class VenueMessageListener extends GluePersistenceService implements
	FeedMessageListener<Venue> {

    static final Logger LOG = LoggerFactory
	    .getLogger(VenueMessageListener.class);

    public VenueMessageListener() {
	super();
    }

    @Override
    public void newMessage(final Venue venue) throws Exception {

	try {
	    // Begin transaction
	    begin();

	    // Search for an existing venue
	    Venue persistentVenue = getVenueDAO().findDuplicate(venue);

	    if (persistentVenue == null) {
		LOG.info("Inserting " + venue);
		persistentVenue = getVenueDAO().create(venue);
	    } else {
		LOG.info("Venue already exists = " + venue);
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
