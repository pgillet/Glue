package com.glue.webapp.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.glue.domain.Event;
import com.glue.domain.Event_;
import com.glue.domain.Venue;
import com.glue.domain.Venue_;
import com.glue.persistence.GluePersistenceService;

@ManagedBean
@ApplicationScoped
public class MetricsBean {

    @PersistenceContext(unitName = GluePersistenceService.PERSISTENCE_UNIT)
    private transient EntityManager em;

    /**
     * Returns the number of upcoming events that are not withdrawn.
     * 
     * @return
     */
    public long getEventCount() {
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Long> cq = cb.createQuery(Long.class);
	Root<Event> event = cq.from(Event.class);
	cq.select(cb.count(event));

	List<Predicate> conjunction = new ArrayList<>();

	// Events that are not withdrawn and not closed
	conjunction.add(cb.isFalse(event.get(Event_.withdrawn)));
	conjunction.add(cb.and(cb.greaterThan(event.get(Event_.stopTime),
		new Date())));

	cq.where(conjunction.toArray(new Predicate[conjunction.size()]));

	return em.createQuery(cq).getSingleResult();
    }

    /**
     * Returns the number of reference venues.
     * 
     * @return
     */
    public long getVenueCount() {
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Long> cq = cb.createQuery(Long.class);
	Root<Venue> venue = cq.from(Venue.class);

	//GD: Only if the venue is a reference one.
	cq.where(cb.isTrue(venue.get(Venue_.reference)));

	cq.select(cb.countDistinct(venue));
	return em.createQuery(cq).getSingleResult();
    }

}
