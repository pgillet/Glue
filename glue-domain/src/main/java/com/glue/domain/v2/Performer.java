package com.glue.domain.v2;

import java.util.Date;
import java.util.List;

/**
 * Performer object.
 */
public class Performer {

    /**
     * Performer ID.
     */
    private String id;

    /**
     * Performer name.
     */
    private String name;

    /**
     * Short performer bio.
     */
    private String shortBio;

    /**
     * Long performer bio
     */

    private String longBio;

    /**
     * Date performer was created.
     */
    private Date created;

    /**
     * Username that created the performer.
     */
    private String creator;

    /**
     * Whether the performer was withdrawn or not
     */
    private boolean withdrawn;

    private String withdrawnNote;

    private List<Link> links;

    private List<Comment> comments;

    private List<Event> events;

    private List<Image> images;

    private List<Tag> tags;

    private List<Property> properties;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    /**
     * List of performer comments.
     * 
     * @return the comments
     */
    public List<Comment> getComments() {
	return comments;
    }

    /**
     * List of performer comments.
     * 
     * @param comments
     *            the comments to set
     */
    public void setComments(List<Comment> comments) {
	this.comments = comments;
    }

    /**
     * Date performer was created.
     * 
     * @return the created
     */
    public Date getCreated() {
	return created;
    }

    /**
     * Date performer was created.
     * 
     * @param created
     *            the created to set
     */
    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     * Get the creator of the performer.
     * 
     * @return the creator
     */
    public String getCreator() {
	return creator;
    }

    /**
     * Set the performer creator.
     * 
     * @param creator
     *            the creator to set
     */
    public void setCreator(String creator) {
	this.creator = creator;
    }

    /**
     * List of performer events.
     * 
     * @return the events
     */
    public List<Event> getEvents() {
	return events;
    }

    /**
     * Set the performer events.
     * 
     * @param events
     *            the events to set
     */
    public void setEvents(List<Event> events) {
	this.events = events;
    }

    /**
     * List the performer images.
     * 
     * @return the images
     */
    public List<Image> getImages() {
	return images;
    }

    /**
     * Set the performer images.
     * 
     * @param images
     *            the images to set
     */
    public void setImages(List<Image> images) {
	this.images = images;
    }

    /**
     * Return a list of performer links.
     * 
     * @return the links
     */
    public List<Link> getLinks() {
	return links;
    }

    /**
     * Set the performer links.
     * 
     * @param links
     *            the links to set
     */
    public void setLinks(List<Link> links) {
	this.links = links;
    }

    /**
     * Performer long bio.
     * 
     * @return the longBio
     */
    public String getLongBio() {
	return longBio;
    }

    /**
     * Performer long bio.
     * 
     * @param longBio
     *            the longBio to set
     */
    public void setLongBio(String longBio) {
	this.longBio = longBio;
    }

    /**
     * Performer name.
     * 
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Set the performer name.
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Set the performer short bio.
     * 
     * @return the shortBio
     */
    public String getShortBio() {
	return shortBio;
    }

    /**
     * Performer short bio.
     * 
     * @param shortBio
     *            the shortBio to set
     */
    public void setShortBio(String shortBio) {
	this.shortBio = shortBio;
    }

    /**
     * List of performer tags.
     * 
     * @return the tags
     */
    public List<Tag> getTags() {
	return tags;
    }

    /**
     * List of performer tags.
     * 
     * @param tags
     *            the tags to set
     */
    public void setTags(List<Tag> tags) {
	this.tags = tags;
    }

    /**
     * Whether the performer is withdrawn.
     * 
     * @return the withdrawn
     */
    public boolean isWithdrawn() {
	return withdrawn;
    }

    /**
     * Whether the performer is withdrawn.
     * 
     * @param withdrawn
     *            the withdrawn to set
     */
    public void setWithdrawn(boolean withdrawn) {
	this.withdrawn = withdrawn;
    }

    /**
     * Return the withdrawn note.
     * 
     * @return the withdrawnNote
     */
    public String getWithdrawnNote() {
	return withdrawnNote;
    }

    /**
     * Set the withdrawn note. 
     * 
     * 
     * String)
     * 
     * @param withdrawnNote
     *            the withdrawnNote to set
     */
    public void setWithdrawnNote(String withdrawnNote) {
	this.withdrawnNote = withdrawnNote;
    }

    /**
     * Return the properties.
     * 
     * @return the properties
     */
    public List<Property> getProperties() {
	return properties;
    }

    /**
     * Set the properties.
     * 
     * @param properties
     *            the properties to set
     */
    public void setProperties(List<Property> properties) {
	this.properties = properties;
    }

}
