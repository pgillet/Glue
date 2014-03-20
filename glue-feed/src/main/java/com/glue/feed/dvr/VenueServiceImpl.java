package com.glue.feed.dvr;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Venue;
import com.glue.domain.Venue_;
import com.glue.feed.nominatim.NominatimRequester;
import com.glue.persistence.GluePersistenceService;
import com.glue.persistence.VenueDAO;

public class VenueServiceImpl extends GluePersistenceService implements
	VenueService {

    static final Logger LOG = LoggerFactory.getLogger(VenueServiceImpl.class);

    private NominatimRequester nr = new NominatimRequester();

    public VenueServiceImpl() {
	super();
    }

    @Override
    public List<Venue> getUnresolvedVenues(int limit) {
	EntityManager em = begin();
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Venue> cq = cb.createQuery(Venue.class);
	Root<Venue> venue = cq.from(Venue.class);

	cq.where(cb.and(cb.isNull(venue.get(Venue_.parent)),
		cb.isFalse(venue.get(Venue_.reference))));

	cq.select(venue);
	TypedQuery<Venue> q = em.createQuery(cq).setMaxResults(limit);
	List<Venue> venues = q.getResultList();

	commit();

	return venues;
    }

    @Override
    public Venue resolve(Venue venue) {

	String query = venue.getName() + ", " + venue.getCity();

	Venue venueRef = nr.search(query);

	if (venueRef != null) {
	    venueRef.setReference(true);
	    venue.setParent(venueRef);
	} else if (venue.getLatitude() != 0.0d && venue.getLongitude() != 0.0d) {
	    // Fallback: we consider that a venue with specified lat/long is
	    // good enough
	    // TODO: should we have Double objects for lat/long ?

	    venue.setReference(true);
	    venueRef = venue;
	}

	if (venueRef != null) {
	    VenueDAO venueDAO = getVenueDAO();

	    begin();
	    venueDAO.update(venue);
	    commit();
	}

	return venueRef;
    }
}
