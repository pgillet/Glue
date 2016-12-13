package com.glue.feed.service;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.Extractor;
import com.glue.bot.HtmlMapper;
import com.glue.domain.Event;
import com.glue.domain.Occurrence;
import com.glue.domain.Tag;
import com.glue.domain.Venue;
import com.glue.persistence.GluePersistenceService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EventPersister extends GluePersistenceService implements Extractor {

	static final Logger LOG = LoggerFactory.getLogger(EventPersister.class);

	private HtmlMapper<Event> mappingStrategy;

	/**
	 * For JSON pretty printing.
	 */
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public EventPersister(HtmlMapper<Event> mappingStrategy) {
		super();
		this.mappingStrategy = mappingStrategy;
	}

	@Override
	public void process(Element e) throws Exception {

		try {

			Event event = mappingStrategy.parse(e);

			if (event == null) {
				return;
			}
			Venue venue = event.getVenue();
			if (venue == null) {
				LOG.trace("Events without a venue are not allowed");
				return;
			}

			String jsonObj = gson.toJson(event);
			LOG.info("Event parsed = " + jsonObj);

			// Begin transaction
			begin();

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
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			rollback();
			throw ex;
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
