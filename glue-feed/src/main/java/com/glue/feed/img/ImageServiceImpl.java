package com.glue.feed.img;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.Event_;
import com.glue.domain.Image;
import com.glue.domain.Image_;
import com.glue.domain.Performer;
import com.glue.domain.Venue;
import com.glue.persistence.GluePersistenceService;

public class ImageServiceImpl extends GluePersistenceService implements
	ImageService {

    static final Logger LOG = LoggerFactory.getLogger(ImageServiceImpl.class);

    public ImageServiceImpl() {
	super();
    }

    @Override
    public List<Event> getEventsCreatedAfter(Date d) {
	EntityManager em = begin();
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Event> cq = cb.createQuery(Event.class);
	Root<Event> event = cq.from(Event.class);
	
	event.fetch(Event_.images);
	Join<Event, Image> image = event.join(Event_.images);
	
	cq.where(cb.and(cb.greaterThanOrEqualTo(event.get(Event_.created), d),
		cb.isTrue(image.get(Image_.sticky))));

	cq.select(event);
	TypedQuery<Event> q = em.createQuery(cq);
	List<Event> events = q.getResultList();

	commit();

	return events;
    }

    @Override
    public List<Venue> getVenuesCreatedAfter(Date d) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Performer> getPerformersCreatedAfter(Date d) {
	// TODO Auto-generated method stub
	return null;
    }

}
