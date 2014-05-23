package com.glue.persistence;

import java.util.List;

import com.glue.domain.Category;
import com.glue.domain.Comment;
import com.glue.domain.Image;
import com.glue.domain.Link;
import com.glue.domain.Performer;
import com.glue.domain.Property;
import com.glue.domain.Tag;

public class PerformerDAO extends AbstractDAO<Performer> implements
	BaseOperations {

    public PerformerDAO() {
	super();
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
