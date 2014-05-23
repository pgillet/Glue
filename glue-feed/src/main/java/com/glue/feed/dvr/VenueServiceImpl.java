package com.glue.feed.dvr;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Venue;
import com.glue.domain.Venue_;
import com.glue.feed.geo.GeoLocation;
import com.glue.feed.nominatim.NominatimRequester;
import com.glue.feed.sim.MetricHandler;
import com.glue.feed.sim.SimilarityMetric;
import com.glue.feed.sim.VenueSimilarityMetric;
import com.glue.persistence.GluePersistenceService;
import com.glue.persistence.VenueDAO;

public class VenueServiceImpl extends GluePersistenceService implements
	VenueService {

    private static final float SIMILARITY_THRESHOLD = 0.9f;

    static final Logger LOG = LoggerFactory.getLogger(VenueServiceImpl.class);

    private NominatimRequester nr = new NominatimRequester();
    private SimilarityMetric<Venue> metric = new VenueSimilarityMetric();
    private MetricHandler<Venue> metricHandler = new MetricHandler<>(metric,
	    SIMILARITY_THRESHOLD);

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

    /**
     * Algorithm:
     * <ol>
     * <li>
     * If the given venue is already a reference venue, the method simply
     * returns it.</li>
     * <li>If the given venue has specified lat/long, search in database for
     * reference venues in a bounding box centered on these coordinates. Compute
     * the similarity between each of them and the given venue, and keep only
     * the best candidate. If its score is greater or equal a given threshold,
     * then the candidate becomes the parent of the given venue in the database,
     * and the parent is returned.</li>
     * <li>If the given venue has no coordinates or if the previous step has
     * failed, request the MapQuest Nominatim search service. The first match
     * becomes the parent of the given venue, and is returned.</li>
     * <li>If the given venue has specified lat/long but is still unresolved, we
     * consider that it is good enough to be promoted as a reference venue.</li>
     * <li>Returns null otherwise.</li>
     * </ol>
     * 
     */
    @Override
    public Venue resolve(Venue venue) {

	if (venue.isReference()) {
	    return venue;
	}

	Venue venueRef = null;
	VenueDAO venueDAO = getVenueDAO();
	final double distance = 2; // 2 kms around
	boolean hasLatLong = (venue.getLatitude() != 0.0d && venue
		.getLongitude() != 0.0d);
	GeoLocation location = null;

	if (hasLatLong) {

	    location = GeoLocation.fromDegrees(venue.getLatitude(),
		    venue.getLongitude());

	    long start = System.currentTimeMillis();
	    List<Venue> del = findWithinDistance(location, distance);
	    long end = System.currentTimeMillis();
	    LOG.info("findWithinDistance took " + (end - start) + " ms");
	    
	    // Filter venues: keep only reference venues and remove the venue to
	    // be resolved.
	    List<Venue> candidates = new ArrayList<>(del);
	    ListIterator<Venue> iter = candidates.listIterator();
	    while (iter.hasNext()) {
		Venue other = iter.next();
		if (!other.isReference()
			|| (other.getLatitude() == venue.getLatitude() && other
				.getLongitude() == venue.getLongitude())) {
		    iter.remove();
		}
	    }

	    venueRef = metricHandler.getBestMatchOver(venue, candidates);
	}

	if (venueRef == null) {

	    // Request Nominatim
	    String query = venue.getName() + ", " + venue.getCity();
	    GeoLocation[] boundingCoordinates = hasLatLong ? location
		    .boundingCoordinates(15) // 15 kms around
		    : null;

	    venueRef = nr.search(query, boundingCoordinates);

	    if (venueRef != null) {
		// Case where the reference venue is already persisted but has
		// not been found within the given distance. This can happen
		// when the venue to resolve is located approximately in the
		// center city.
		Venue match = venueDAO.findDuplicate(venueRef);
		if (match != null) {
		    venueRef = match;
		}

	    } else if (hasLatLong) {
		// Fallback: we consider that a venue with specified lat/long is
		// good enough
		venueRef = venue;
	    }
	}

	if (venueRef != null) {

	    if (venue.getId().equals(venueRef.getId())) {
		// The venue has been promoted has a reference venue
		venue.setReference(true);
	    } else {
		venueRef.setReference(true);
		venue.setParent(venueRef);
	    }

	    begin();
	    venueDAO.update(venue);
	    commit();
	}

	return venueRef;
    }

    public List<Venue> findWithinDistance(GeoLocation location,
	    double distance) {

	GeoLocation[] boundingCoordinates = location.boundingCoordinates(distance);
	boolean meridian180WithinDistance = boundingCoordinates[0]
		.getLongitudeInRadians() > boundingCoordinates[1]
		.getLongitudeInRadians();

	EntityManager em = begin();

	String sqlString = "SELECT * FROM Venue WHERE (latitude >= ?1 AND latitude <= ?2) AND (longitude >= ?3 "
		+ (meridian180WithinDistance ? "OR" : "AND")
		+ " longitude <= ?4) AND "
		+ "acos(sin(?5) * sin(radians(latitude)) + cos(?6) * cos(radians(latitude)) * cos(radians(longitude) - ?7)) <= ?8";

	Query query = em.createNativeQuery(sqlString, Venue.class);

	query.setParameter(1, boundingCoordinates[0].getLatitudeInDegrees());
	query.setParameter(2, boundingCoordinates[1].getLatitudeInDegrees());
	query.setParameter(3, boundingCoordinates[0].getLongitudeInDegrees());
	query.setParameter(4, boundingCoordinates[1].getLongitudeInDegrees());
	query.setParameter(5, location.getLatitudeInRadians());
	query.setParameter(6, location.getLatitudeInRadians());
	query.setParameter(7, location.getLongitudeInRadians());
	query.setParameter(8, distance / GeoLocation.EARTH_RADIUS);

	List<Venue> results = (List<Venue>) query.getResultList();

	commit();

	return results;
    }

}
