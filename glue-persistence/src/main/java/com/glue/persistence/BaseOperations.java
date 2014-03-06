package com.glue.persistence;

import java.util.List;

import com.glue.domain.v2.Category;
import com.glue.domain.v2.Comment;
import com.glue.domain.v2.Image;
import com.glue.domain.v2.Link;
import com.glue.domain.v2.Performer;
import com.glue.domain.v2.Property;
import com.glue.domain.v2.Tag;

public interface BaseOperations {

    /**
     * Adds a category.
     * 
     * @param id
     * @param category
     */
    void addCategory(String id, Category category);

    /**
     * Adds a comment.
     * 
     * @param id
     * @param comment
     */
    void addComment(String id, Comment comment);

    /**
     * Adds an image.
     * 
     * @param id
     * @param image
     */
    void addImage(String id, Image image);

    /**
     * Adds a link.
     * 
     * @param id
     * @param link
     */
    void addLink(String id, Link link);

    /**
     * Adds a performer.
     * 
     * @param id
     * @param performer
     */
    void addPerformer(String id, Performer performer);

    /**
     * Adds a property.
     * 
     * @param id
     * @param property
     * @return
     */
    int addProperty(String id, Property property);

    /**
     * Adds tags.
     * 
     * @param id
     * @param tagList
     */
    void addTags(String id, List<Tag> tagList);

    /**
     * Removes a category.
     * 
     * @param id
     * @param category
     */
    void deleteCategory(String id, Category category);

    /**
     * Deletes a comment.
     * 
     * @param comment
     */
    void deleteComment(Comment comment);

    /**
     * Removes the image.
     * 
     * @param id
     * @param image
     */
    void deleteImage(String id, Image image);

    /**
     * Removes a link.
     * 
     * @param link
     */
    void deleteLink(Link link);

    /**
     * Removes a performer.
     * 
     * @param id
     * @param performer
     */
    void deletePerformer(String id, Performer performer);

    /**
     * Deletes a property.
     * 
     * @param id
     * @param property
     */
    void deleteProperty(String id, Property property);

    /**
     * Deletes tags.
     * 
     * @param id
     * @param tagList
     */
    void deleteTags(String id, List<Tag> tagList);

    /**
     * Returns a list of properties.
     * 
     * @param id
     * @return
     */
    List<Property> getProperties(String id);

    /**
     * Lists the tags for a given id.
     * 
     * @param id
     * @return
     */
    List<Tag> getTags(String id);

    /**
     * Modifies a comment.
     * 
     * @param comment
     */
    void modifyComment(Comment comment);

    /**
     * Restores (un-withdraws) an object.
     * 
     * @param id
     */
    void restore(String id);

    /**
     * Withdraws an object.
     * 
     * @param id
     * @param withdrawNote
     */
    void withdraw(String id, String withdrawNote);

}
