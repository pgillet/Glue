package com.glue.persistence;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import com.glue.domain.Category;
import com.glue.domain.Comment;
import com.glue.domain.Image;
import com.glue.domain.Link;
import com.glue.domain.Performer;
import com.glue.domain.Property;
import com.glue.domain.Tag;
import com.glue.domain.Venue;

public class VenueDAO extends AbstractDAO<Venue> implements BaseOperations {

    public VenueDAO() {
	super();
    }

    @Override
    public Venue create(Venue v) {
	// Creation date
	v.setCreated(new Date(System.currentTimeMillis()));
	return super.create(v);
    }

    public Venue findDuplicate(Venue v) {

	Query query = em
		.createQuery(
			"SELECT v FROM Venue v WHERE v.name = :name AND v.city = :city",
			type).setParameter("name", v.getName())
		.setParameter("city", v.getCity());

	Venue other = PersistenceHelper.getSingleResultOrNull(query);

	return other;
    }

    public boolean hasDuplicate(Venue venue) {
	return (findDuplicate(venue) != null);
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
