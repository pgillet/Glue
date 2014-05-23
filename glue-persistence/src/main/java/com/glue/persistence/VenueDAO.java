package com.glue.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.glue.domain.Category;
import com.glue.domain.Comment;
import com.glue.domain.Image;
import com.glue.domain.Link;
import com.glue.domain.Performer;
import com.glue.domain.Property;
import com.glue.domain.Tag;
import com.glue.domain.Venue;
import com.glue.domain.Venue_;

public class VenueDAO extends AbstractDAO<Venue> implements BaseOperations {

    public VenueDAO() {
	super();
    }

    /**
     * @see #findDuplicate(Venue, boolean)
     * @param v
     * @return
     */
    public Venue findDuplicate(Venue v) {
        return findDuplicate(v, false);
    }

    /**
     * Returns the persistent venue with the same name and city as the given
     * venue, or null if no such venue exists. The second argument tells whether
     * the persistent venue must be a reference venue or not.
     * 
     * @param v
     * @param reference
     * @return
     */
    public Venue findDuplicate(Venue v, boolean reference) {

	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Venue> cq = cb.createQuery(Venue.class);
	Root<Venue> venue = cq.from(Venue.class);
	cq.select(venue);

	// Matching is based on name, city.
	List<Predicate> conjunction = new ArrayList<>();

	conjunction.add(cb.equal(venue.get(Venue_.name), v.getName()));
	conjunction.add(cb.equal(venue.get(Venue_.city), v.getCity()));
	conjunction.add(cb.equal(venue.get(Venue_.reference), reference));

	cq.where(conjunction.toArray(new Predicate[conjunction.size()]));

	TypedQuery<Venue> q = em.createQuery(cq);

	Venue result = PersistenceHelper.getSingleResultOrNull(q);

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
