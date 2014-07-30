package com.glue.webapp.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.glue.domain.Event;
import com.glue.domain.EventCategory;
import com.glue.domain.Event_;
import com.glue.domain.Venue;
import com.glue.domain.Venue_;
import com.glue.persistence.GluePersistenceService;
import com.glue.persistence.PersistenceHelper;
import com.glue.webapp.search.AbstractPaginatedSearch;

public class VenueController extends AbstractPaginatedSearch<List<Event>> {

    @PersistenceContext(unitName = GluePersistenceService.PERSISTENCE_UNIT)
    private transient EntityManager em;

    /**
     * The identifier of the venue around which the search is articulated.
     */
    private String venueId;

    private String queryString;

    private Date startDate;

    private Date endDate;

    private List<String> categories = new ArrayList<>();

    /**
     * @return the venueId
     */
    public String getVenueId() {
	return venueId;
    }

    /**
     * @param venueId
     *            the venueId to set
     */
    public void setVenueId(String venueId) {
	this.venueId = venueId;
    }

    public String getQueryString() {
	return queryString;
    }

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

    public List<String> getCategories() {
	return categories;
    }

    public void setCategories(List<String> categories) {
	this.categories = categories;
    }

    @Override
    public List<Event> search() throws InternalServerException {

	Venue venue = getVenue(this.venueId);
	Venue parent = venue.getParent();

	String id = (parent != null) ? parent.getId() : this.venueId;

	if (startDate == null) {
	    DateTime today = new DateTime(DateTimeZone.UTC);
	    today = today.withTimeAtStartOfDay();
	    startDate = today.toDate();
	}

	this.totalRows = getEventCount(id);
	List<Event> events = getEvents(id);

	// Summarizing
	for (Event event : events) {
	    String summary = EventUtils.summarize(event.getDescription());
	    event.setSummary(summary);
	}

	return events;
    }

    /**
     * Returns the venue with the given id, with its reference venue if any.
     * 
     * @param venueId
     * @return
     */
    public Venue getVenue(String venueId) {

	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Venue> cq = cb.createQuery(Venue.class);
	Root<Venue> venue = cq.from(Venue.class);

	venue.fetch(Venue_.parent.getName(), JoinType.LEFT);

	cq.where(cb.equal(venue.get(Venue_.id), venueId));

	cq.select(venue);
	TypedQuery<Venue> q = em.createQuery(cq);

	Venue v = PersistenceHelper.getSingleResultOrNull(q);

	return v;
    }

    public long getEventCount(String venueId) {
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Long> cq = cb.createQuery(Long.class);
	Root<Event> event = cq.from(Event.class);
	cq.select(cb.count(event));
	cq.where(getWhereClause(venueId, cb, event));
	return em.createQuery(cq).getSingleResult();
    }

    public List<Event> getEvents(String venueId) {
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Event> cq = cb.createQuery(Event.class);
	Root<Event> event = cq.from(Event.class);

	cq.select(event).distinct(true);
	event.fetch(Event_.images.getName(), JoinType.LEFT);
	event.fetch(Event_.links.getName(), JoinType.LEFT);
	// event.fetch(Event_.venue);

	cq.where(getWhereClause(venueId, cb, event));

	// order by startTime. We assume that events for a single venue do not
	// overlap.
	cq.orderBy(cb.asc(event.get(Event_.startTime)));

	List<Event> events = em.createQuery(cq).setMaxResults(this.rowsPerPage)
		.setFirstResult(this.start).getResultList();

	return events;
    }

    protected Predicate[] getWhereClause(String venueId, CriteriaBuilder cb,
	    Root<Event> event) {
	Join<Event, Venue> venue = event.join(Event_.venue);
	Join<Venue, Venue> parent = venue.join(Venue_.parent.getName(),
		JoinType.LEFT);

	List<Predicate> conjunction = new ArrayList<>();

	// Relatively to the start date
	if (startDate != null) {
	    // Events that have not yet started
	    Predicate notYetStarted = cb.greaterThanOrEqualTo(
		    event.get(Event_.startTime), startDate);

	    // or events already started but not yet finished
	    Predicate notYetFinished = cb.greaterThanOrEqualTo(
		    event.get(Event_.stopTime), startDate);

	    conjunction.add(cb.or(notYetStarted, notYetFinished));
	}

	// Relatively to the end date
	if (endDate != null) {
	    // Events that have not yet started
	    Predicate notYetStarted = cb.lessThanOrEqualTo(
		    event.get(Event_.startTime), endDate);

	    // or events already started but not yet finished
	    Predicate notYetFinished = cb.lessThanOrEqualTo(
		    event.get(Event_.stopTime), endDate);

	    conjunction.add(cb.or(notYetStarted, notYetFinished));
	}

	// Categories
	if (!categories.isEmpty()) {
	    List<Predicate> disjunction = new ArrayList<>();
	    for (String cat : categories) {
		disjunction.add(cb.equal(event.get(Event_.category),
			EventCategory.valueOf(cat)));
	    }

	    conjunction.add(cb.or(disjunction.toArray(new Predicate[disjunction
		    .size()])));
	}

	conjunction.add(cb.or(cb.equal(venue.get(Venue_.id), venueId),
		cb.equal(parent.get(Venue_.id), venueId)));

	return conjunction.toArray(new Predicate[conjunction.size()]);
    }

}
