package com.glue.webapp.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.solr.client.solrj.response.FacetField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.Venue;
import com.glue.persistence.EventDAO;
import com.glue.persistence.GluePersistenceService;
import com.glue.persistence.VenueDAO;
import com.glue.webapp.search.AbstractPaginatedSearch;
import com.glue.webapp.search.SearchEngine;

public class EventController extends AbstractPaginatedSearch<List<Event>> {

	@PersistenceContext(unitName = GluePersistenceService.PERSISTENCE_UNIT)
	private EntityManager em;

	@Inject
	private EventDAO eventDAO;

	@Inject
	private VenueDAO venueDAO;

	static final Logger LOG = LoggerFactory.getLogger(EventController.class);

	@Inject
	private transient SearchEngine<Event> engine;

	private String queryString;
	private Date startDate;
	private Date endDate;
	private String location;
	private double latitude;
	private double longitude;
	private String bbox;
	private List<FacetField.Count> filterQueries;
	private List<FacetField> facetFields;

	private List<String> categories = new ArrayList<>();

	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * @param queryString
	 *            the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getBoundingBox() {
		return bbox;
	}

	/**
	 * Sets a string with bounding box coordinates in a
	 * 'southwest_lng,southwest_lat,northeast_lng,northeast_lat' format, for
	 * finding everything in a rectangular area, such as the area covered by a
	 * map the user is looking at.
	 */
	public void setBoundingBox(String bbox) {
		this.bbox = bbox;
	}

	/**
	 * @return the categories
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<FacetField.Count> getFilterQueries() {
		return filterQueries;
	}

	public void setFilterQueries(List<FacetField.Count> filterQueries) {
		this.filterQueries = filterQueries;
	}

	public List<FacetField> getFacetFields() {
		return facetFields;
	}

	public void setFacetFields(List<FacetField> facetFields) {
		this.facetFields = facetFields;
	}

	public List<Event> search() throws InternalServerException {

		// The underlying search engine returns only partial streams (i.e. only
		// the properties that are actually indexed)
		engine.setQueryString(queryString);
		engine.setCategories(categories.toArray(new String[categories.size()]));
		engine.setStartTime(startDate);
		engine.setEndDate(endDate);
		engine.setStart(start);
		engine.setRows(rowsPerPage);
		engine.setLatitude(latitude);
		engine.setLongitude(longitude);
		engine.setLocation(location);
		engine.setBoundingBox(bbox);
		engine.setFilterQueries(filterQueries);

		final Map<String, Event> me = (Map<String, Event>) engine.searchAsMap();

		// Stores the total number of found results
		totalRows = engine.getNumFound();

		// Facet fields
		facetFields = engine.getFacetFields();

		final Set<String> ids = me.keySet();

		try {
			List<Event> events = eventDAO.findAll(ids);

			// Highlighting
			for (Event event : events) {
				String summary = me.get(event.getId()).getSummary();
				if (summary == null) {
					summary = EventUtils.summarize(event.getDescription());
				}
				event.setSummary(summary);
			}

			return events;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new InternalServerException(e);
		}
	}

	/**
	 * This method should be moved in another controller dedicated to event
	 * creation.
	 * 
	 * @param event
	 * @param venue
	 * @param admin
	 * @throws InternalServerException
	 */
	public Event create(final Event event) throws InternalServerException {
		Event persistedEvent = null;
		try {
			// Begin transaction ?

			Venue venue = event.getVenue();
			if (venue == null) {
				throw new IllegalArgumentException(
						"Events without a venue are not allowed");
			}

			// Search for an existing venue
			Venue persistentVenue = venueDAO.findDuplicate(venue);
			if (persistentVenue == null) {
				LOG.info("Inserting " + venue);
				persistentVenue = venueDAO.create(venue);
			} else {
				LOG.info("Venue already exists = " + venue);
			}
			event.setVenue(persistentVenue);

			if (!eventDAO.hasDuplicate(event)) {
				LOG.info("Inserting " + event);
				persistedEvent = eventDAO.update(event);

			} else {
				LOG.info("Event already exists = " + event);
			}

			// End transaction ?
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			// Rollback ?
			throw new InternalServerException(e);
		} finally {

		}
		return persistedEvent;
	}

	public Event search(final String id) throws InternalServerException {
		try {
			Event event = eventDAO.findWithFullInfo(id);

			return event;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new InternalServerException(e);
		}
	}
}
