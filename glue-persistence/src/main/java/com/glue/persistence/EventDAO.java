package com.glue.persistence;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import com.glue.domain.Category;
import com.glue.domain.Comment;
import com.glue.domain.Event;
import com.glue.domain.Event_;
import com.glue.domain.Image;
import com.glue.domain.Link;
import com.glue.domain.Performer;
import com.glue.domain.Property;
import com.glue.domain.Tag;

public class EventDAO extends AbstractDAO<Event> implements BaseOperations {

    public EventDAO() {
	super();
    }

    /**
     * TODO: may be factorized in AbstractDAO with the help of the Glob
     * interface.
     */
    @Override
    public Event create(Event e) {
	// Creation date
	e.setCreated(new Date(System.currentTimeMillis()));
	return super.create(e);
    }

    /**
     * TODO: may be factorized in AbstractDAO with the help of the Glob
     * interface.
     */
    @Override
    public Event update(Event e) {
	// Last modified
	e.setCreated(new Date(System.currentTimeMillis()));
	return super.update(e);
    }

    public List<Event> findAll(Collection<String> ids) {

	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Event> cq = cb.createQuery(Event.class);
	Root<Event> event = cq.from(Event.class);
	event.fetch(Event_.images.getName(), JoinType.LEFT);

	cq.where(event.get(Event_.id).in(ids));
	cq.select(event);
	TypedQuery<Event> q = em.createQuery(cq);

	List<Event> events = q.getResultList();

	return events;
    }

    public List<Event> findBetween(Date start, Date end) throws SQLException {
	List<Event> events = em.createNamedQuery("findBetween", type)
		.setParameter("start", start).setParameter("end", end)
		.getResultList();

	return events;
    }

    /**
     * This method assumes that the given event has an already persisted venue.
     * 
     * @param event
     * @return
     */
    public Event findDuplicate(Event event) {
	Query query = em.createNamedQuery("findDuplicate", type)
		.setParameter("title", event.getTitle())
		.setParameter("startTime", event.getStartTime())
		.setParameter("venueId", event.getVenue().getId());

	Event other = PersistenceHelper.getSingleResultOrNull(query);

	return other;
    }

    public boolean hasDuplicate(Event event) {
	return (findDuplicate(event) != null);
    }

    /**
     * List the images for the given id.
     * 
     * @param id
     * @return
     */
    public Event findWithImages(String id) {

	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Event> cq = cb.createQuery(Event.class);
	Root<Event> event = cq.from(Event.class);

	event.fetch(Event_.images.getName(), JoinType.LEFT);

	cq.where(cb.equal(event.get(Event_.id), id));
	cq.select(event);
	TypedQuery<Event> q = em.createQuery(cq);

	Event result = PersistenceHelper.getSingleResultOrNull(q);

	return result;
    }

    @Override
    public void addCategory(String id, Category category) {
	// TODO Auto-generated method stub

    }

    @Override
    public void addComment(String id, Comment comment) {
	// TODO Auto-generated method stub

    }

    @Override
    public void addImage(String id, Image image) {
	// TODO Auto-generated method stub

    }

    @Override
    public void addLink(String id, Link link) {
	// TODO Auto-generated method stub

    }

    @Override
    public void addPerformer(String id, Performer performer) {
	// TODO Auto-generated method stub

    }

    @Override
    public int addProperty(String id, Property property) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public void addTags(String id, List<Tag> tagList) {
	// TODO Auto-generated method stub

    }

    @Override
    public void deleteCategory(String id, Category category) {
	// TODO Auto-generated method stub

    }

    @Override
    public void deleteComment(Comment comment) {
	// TODO Auto-generated method stub

    }

    @Override
    public void deleteImage(String id, Image image) {
	// TODO Auto-generated method stub

    }

    @Override
    public void deleteLink(Link link) {
	// TODO Auto-generated method stub

    }

    @Override
    public void deletePerformer(String id, Performer performer) {
	// TODO Auto-generated method stub

    }

    @Override
    public void deleteProperty(String id, Property property) {
	// TODO Auto-generated method stub

    }

    @Override
    public void deleteTags(String id, List<Tag> tagList) {
	// TODO Auto-generated method stub

    }

    @Override
    public List<Property> getProperties(String id) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Tag> getTags(String id) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void modifyComment(Comment comment) {
	// TODO Auto-generated method stub

    }

    @Override
    public void restore(String id) {
	// TODO Auto-generated method stub

    }

    @Override
    public void withdraw(String id, String withdrawNote) {
	// TODO Auto-generated method stub

    }
}
