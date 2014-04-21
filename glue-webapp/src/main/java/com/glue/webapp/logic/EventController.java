package com.glue.webapp.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.Venue;
import com.glue.persistence.EventDAO;
import com.glue.persistence.GluePersistenceService;
import com.glue.persistence.VenueDAO;
import com.glue.webapp.search.PageIterator;
import com.glue.webapp.search.SearchEngine;

public class EventController implements
 PageIterator<List<Event>>,
	Serializable {

    @PersistenceContext(unitName = GluePersistenceService.PERSISTENCE_UNIT)
    private transient EntityManager em;

    @Inject
    private transient EventDAO eventDAO;

    @Inject
    private transient VenueDAO venueDAO;

    private static final String ELLIPSIS = "...";

    private static final int SUMMARY_LIMIT = 200;

    static final Logger LOG = LoggerFactory.getLogger(EventController.class);

    @Inject
    private transient SearchEngine<Event> engine;

    private int start;

    private int rowsPerPage = PageIterator.DEFAULT_ROWS;

    private long totalRows;

    private String queryString;
    private Date startDate;
    private Date endDate;
    private String location;
    private double latitude;
    private double longitude;

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

	final Map<String, Event> me = (Map<String, Event>) engine.searchAsMap();

	// Stores the total number of found results
	totalRows = engine.getNumFound();

	final Set<String> ids = me.keySet();

	try {
	    List<Event> events = eventDAO.findAll(ids);

	    // Highlighting
	    for (Event event : events) {
		String summary = me.get(event.getId()).getSummary();
		if (summary == null) {
		    String html = event.getDescription();
		    // Keep only text nodes: all HTML will be stripped.
		    String onlytext = Jsoup.clean(html, Whitelist.none());
		    summary = WordUtils.abbreviate(onlytext, SUMMARY_LIMIT, -1,
			    ELLIPSIS);
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
    public void createEvent(final Event event) throws InternalServerException {
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
		eventDAO.update(event);

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
    }

    public Event search(final String id) throws InternalServerException {
	try {
	    Event event = eventDAO.findWithImages(id);

	    return event;
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    throw new InternalServerException(e);
	}
    }

    @Override
    public boolean hasNext() {
	return getPageIndex() < (getTotalPages() - 1);
    }

    @Override
    public List<Event> next() throws InternalServerException,
	    NoSuchElementException {
	if (!hasNext()) {
	    throw new NoSuchElementException();
	}

	start += rowsPerPage;
	return search();
    }

    @Override
    public boolean hasPrevious() {
	return getPageIndex() > 0;
    }

    @Override
    public List<Event> previous() throws InternalServerException,
	    NoSuchElementException {
	if (!hasPrevious()) {
	    throw new NoSuchElementException();
	}

	start -= rowsPerPage;
	return search();
    }

    @Override
    public List<Event> first() throws InternalServerException {
	start = 0;
	return search();
    }

    @Override
    public List<Event> last() throws InternalServerException {
	start = (getTotalPages() - 1) * rowsPerPage; // last page index
	return search();
    }

    @Override
    public List<Event> get(int pageNumber) throws NoSuchElementException {
	// TODO Not yet implemented!
	return null;
    }

    @Override
    public int getStart() {
	return start;
    }

    @Override
    public void setStart(int start) {
	this.start = start;
    }

    @Override
    public int getRowsPerPage() {
	return rowsPerPage;
    }

    @Override
    public void setRowsPerPage(int rowsPerPage) {
	this.rowsPerPage = rowsPerPage;
    }

    @Override
    public long getTotalRows() {
	return totalRows;
    }

    /**
     * For pagination control from request to request.
     */
    public void setTotalRows(long totalRows) {
	this.totalRows = totalRows;
    }

    @Override
    public int getPageIndex() {
	return start / rowsPerPage;
    }

    @Override
    public int getTotalPages() {
	int num = (int) totalRows / rowsPerPage;
	if (totalRows % rowsPerPage > 0) {
	    num++;
	}

	return num;
    }
}
