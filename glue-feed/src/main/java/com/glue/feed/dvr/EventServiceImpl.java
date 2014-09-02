package com.glue.feed.dvr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.Event_;
import com.glue.domain.Venue;
import com.glue.domain.Venue_;
import com.glue.feed.error.ErrorDispatcher;
import com.glue.feed.error.ErrorHandler;
import com.glue.feed.error.ErrorLevel;
import com.glue.feed.error.ErrorListener;
import com.glue.feed.error.ErrorManager;
import com.glue.feed.sim.EventSimilarityMetric;
import com.glue.feed.sim.MetricHandler;
import com.glue.feed.sim.SimilarityMetric;
import com.glue.persistence.GluePersistenceService;

public class EventServiceImpl extends GluePersistenceService implements
	EventService, ErrorHandler, ErrorManager {

    static final Logger LOG = LoggerFactory.getLogger(EventServiceImpl.class);

    private ErrorDispatcher errorDispatcher = new ErrorDispatcher();

    private static final float SIMILARITY_THRESHOLD = 0.85f;

    private SimilarityMetric<Event> metric = new EventSimilarityMetric();
    private MetricHandler<Event> metricHandler = new MetricHandler<>(metric,
	    SIMILARITY_THRESHOLD);

    public EventServiceImpl() {
	super();
    }

    protected long getUnresolvedEventsCount(Date limit) {
	EntityManager em = getEntityManager();

	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Long> cq = cb.createQuery(Long.class);
	Root<Event> event = cq.from(Event.class);
	cq.select(cb.count(event));
	Predicate[] wc = getWhereClause(cb, event, limit);
	cq.where(wc);
	return em.createQuery(cq).getSingleResult();
    }

    @Override
    public List<Event> getUnresolvedEvents(Date limit, int start, int maxResults) {
	EntityManager em = getEntityManager();

	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Event> cq = cb.createQuery(Event.class);
	Root<Event> event = cq.from(Event.class);

	cq.select(event);

	Predicate[] wc = getWhereClause(cb, event, limit);
	cq.where(wc);

	// order by created desc, startTime asc
	// Too slow!
	// cq.orderBy(cb.desc(event.get(Event_.created)),
	// cb.asc(event.get(Event_.startTime)));

	List<Event> events = em.createQuery(cq).setFirstResult(start)
		.setMaxResults(maxResults).getResultList();

	return events;
    }

    protected Predicate[] getWhereClause(CriteriaBuilder cb, Root<Event> event,
	    Date limit) {
	// event.fetch(Event_.venue).fetch(Venue_.parent, JoinType.LEFT);

	Join<Event, Venue> venue = event.join(Event_.venue);
	Join<Venue, Venue> parent = venue.join(Venue_.parent, JoinType.LEFT);

	List<Predicate> conjunction = new ArrayList<>();

	// Select events created after the date limit
	conjunction.add(cb.greaterThanOrEqualTo(event.get(Event_.created),
		limit));

	// Select events that are not already withdrawn
	conjunction.add(cb.isFalse(event.get(Event_.withdrawn)));

	// Select only events whose the venue is resolved
	conjunction.add(cb.or(cb.isTrue(venue.get(Venue_.reference)),
		cb.isTrue(parent.get(Venue_.reference))));

	Predicate[] wc = conjunction.toArray(new Predicate[conjunction.size()]);
	return wc;
    }

    @Override
    public List<Event> getPotentialDuplicates(Event e) {

	EntityManager em = getEntityManager();

	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Event> cq = cb.createQuery(Event.class);
	Root<Event> event = cq.from(Event.class);

	cq.select(event);

	Join<Event, Venue> venue = event.join(Event_.venue);
	Join<Venue, Venue> parent = venue.join(Venue_.parent.getName(),
		JoinType.LEFT);

	List<Predicate> conjunction = new ArrayList<>();

	// Select events that are not the given event
	// Note: selected events may be part of the set of unresolved events
	conjunction.add(cb.notEqual(event.get(Event_.id), e.getId()));

	// Select events from a different source: we consider that a single data
	// source does not contain any duplicates
	// TODO: Should add a column AUTHOR. The event source may be null if a
	// user authored the event.
	if (e.getSource() != null) {
	    conjunction
		    .add(cb.notEqual(event.get(Event_.source), e.getSource()));
	}

	// Select events that are not already withdrawn
	conjunction.add(cb.isFalse(event.get(Event_.withdrawn)));

	// Compare venue
	Venue venueRef = e.getVenue();
	if (!venueRef.isReference()) {
	    venueRef = venueRef.getParent(); // Parent cannot be null here
	}

	conjunction.add(cb.or(cb.equal(venue.get(Venue_.id), venueRef.getId()),
		cb.equal(parent.get(Venue_.id), venueRef.getId())));

	// Compare dates
	DateTime start = new DateTime(e.getStartTime());
	start = start.withTimeAtStartOfDay();

	conjunction.add(cb.greaterThanOrEqualTo(event.get(Event_.startTime),
		start.toDate()));

	if (e.getStopTime() != null) {
	    DateTime stop = new DateTime(e.getStartTime());
	    stop = stop.plusDays(1);
	    stop = stop.withTimeAtStartOfDay();

	    conjunction.add(cb.lessThan(event.get(Event_.stopTime),
		    stop.toDate()));
	} else {
	    DateTime upperStart = start;
	    upperStart = upperStart.plusDays(1);
	    conjunction.add(cb.lessThan(event.get(Event_.startTime),
		    upperStart.toDate()));
	}

	cq.where(conjunction.toArray(new Predicate[conjunction.size()]));

	List<Event> events = em.createQuery(cq).getResultList();

	return events;
    }

    /**
     * TODO: should merge events.
     * 
     * @param e1
     * @param e2
     * @throws Exception
     */
    @Override
    public void resolve(Event e1, Event e2) throws Exception {

	// Reattach: Why do I have to do that!!?
	EntityManager em = getEntityManager();
	e1 = em.merge(e1);
	e2 = em.merge(e2);

	EventPriority p = new EventPriority();

	int p1 = p.getValue(e1);
	int p2 = p.getValue(e2);

	Event disposable;
	if (p1 > p2) {
	    disposable = e2;
	} else {
	    disposable = e1;
	}

	LOG.info("Withdraw event with ID " + disposable.getId() + "\n");

	disposable.setWithdrawn(true);
	disposable.setWithdrawnNote("Duplicate of "
		+ (disposable == e1 ? e2.getId() : e1.getId()));

	getEventDAO().update(disposable);
    }

    public void execute(Date limit) {

	long start = System.currentTimeMillis();

	LOG.info("Reconciling events created after = " + limit);

	// Disable the query cache.
	// The named or positional parameters of a JPQL query can be set to
	// different values across executions. In general, the
	// corresponding cached SQL statement will be re-parameterized
	// accordingly. However, the parameter value itself can determine
	// the SQL query. For example, when a JPQL query compares a relation
	// field for equality against a parameter p, whether
	// the actual value of p is null or not will determine the generated SQL
	// statement. Another example is collection valued
	// parameter for IN expression. Each element of a collection valued
	// parameter results into a SQL parameter. If a collection
	// valued parameter across executions are set to different number of
	// elements, then the parameters of the cached SQL do not
	// correspond. If such situations are encountered while
	// re-parameterizing the cached SQL, the cached version is not reused
	// and the original JPQL query is used to generate a new SQL statement
	// for execution.
	// See
	// http://openjpa.apache.org/builds/2.3.0/apache-openjpa/docs/ref_guide_cache_querysql.html
	setQuerySQLCache(false);

	long count = getUnresolvedEventsCount(limit);
	LOG.info(count + " new events to reconcile");

	int startPosition = 0;
	final int maxResults = 250;

	// A list that stores the ID of resolved events
	List<String> justResolved = new ArrayList<>();

	while (startPosition < count) {

	    LOG.debug(String.format("Events from %d to %d on %d",
		    startPosition,
		    startPosition + maxResults, count));

	    List<Event> events = getUnresolvedEvents(limit, startPosition,
		    maxResults);
	    startPosition += maxResults;

	    for (Event event : events) {

		if (justResolved.contains(event.getId())) {
		    // The current event has been already reconciled with a
		    // previous event in the iteration
		    LOG.debug("Event already resolved with a previous event in the iteration = "
			    + printEvent(event));
		    continue;
		}

		try {

		    begin();

		    List<Event> candidates = getPotentialDuplicates(event);
		    if (candidates.size() > 0) {

			// Reconciliation by pairs
			Event bestMatch = metricHandler.getBestMatchOver(event,
				candidates);
			if (bestMatch != null) {
			    LOG.info("Event to resolve = " + printEvent(event));
			    LOG.info("Found match = " + printEvent(bestMatch));
			    LOG.info("With similarity = "
				    + metric.getSimilarity(event, bestMatch));

			    resolve(event, bestMatch);
			    justResolved.add(bestMatch.getId());
			}

		    }
		    


		    commit();
		} catch (Exception ex) {
		    rollback();
		    LOG.error(ex.getMessage(), ex);
		    fireErrorEvent(ErrorLevel.ERROR, ex.getMessage(), ex, "db",
			    -1);
		}
	    }
	}

	long end = System.currentTimeMillis();
	LOG.info(String.format("Event reconciliation took %d s",
		(int) ((end - start) / 1000)));
    }

    private String printEvent(Event event) {

	Venue venue = event.getVenue();

	String str = "Event [id=" + event.getId() + ", title="
		+ event.getTitle() + ", startTime=" + event.getStartTime()
		+ ", stopTime=" + event.getStopTime() + ", venue="
		+ venue.getName() + ", city=" + venue.getCity() + "]";

	return str;
    }

    @Override
    public ErrorListener[] getErrorListeners() {
	return errorDispatcher.getErrorListeners();
    }

    @Override
    public void addErrorListener(ErrorListener l) {
	errorDispatcher.addErrorListener(l);
    }

    @Override
    public void removeErrorListener(ErrorListener l) {
	errorDispatcher.removeErrorListener(l);
    }

    @Override
    public void fireErrorEvent(ErrorLevel lvl, String message, Throwable cause,
	    String source, int lineNumber) {
	errorDispatcher.fireErrorEvent(lvl, message, cause, source, lineNumber);
    }

    @Override
    public void flush() throws IOException {
	errorDispatcher.flush();
    }

}
