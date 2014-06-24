package com.glue.feed.listener;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.Occurrence;
import com.glue.domain.Tag;
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

	    Venue persistentVenue = persistVenue(venue);
	    event.setVenue(persistentVenue);

	    for (Occurrence occurrence : event.getOccurrences()) {
		Venue occurVenue = occurrence.getVenue();
		Venue other = persistVenue(occurVenue);
		occurrence.setVenue(other);
	    }

	    // Search for existing tags and replace them in event tag list
	    Set<Tag> tags = new HashSet<>();
	    for (Tag tag : event.getTags()) {
		Tag tmpTag = getTagDAO().findDuplicate(tag);
		if (tmpTag == null) {
		    tags.add(tag);
		} else {
		    tags.add(tmpTag);
		}
	    }
	    event.setTags(tags);

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

    protected Venue persistVenue(Venue venue) {
	// Search for an existing venue
	Venue persistentVenue = getVenueDAO().findDuplicate(venue);
	if (persistentVenue == null) {
	    LOG.info("Inserting " + venue);
	    persistentVenue = getVenueDAO().create(venue);
	} else {
	    LOG.info("Venue already exists = " + venue);
	}
	return persistentVenue;
    }
}
