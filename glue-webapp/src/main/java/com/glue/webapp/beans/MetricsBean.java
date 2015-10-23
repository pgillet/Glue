package com.glue.webapp.beans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.glue.domain.Venue;
import com.glue.domain.Venue_;
import com.glue.persistence.EventDAO;
import com.glue.persistence.GluePersistenceService;

@ManagedBean
@ApplicationScoped
public class MetricsBean {

    @PersistenceContext(unitName = GluePersistenceService.PERSISTENCE_UNIT)
    private transient EntityManager em;

    @Inject
    private EventDAO eventDAO;

    public long getEventCount() {
	return eventDAO.countForthcoming();
    }

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
