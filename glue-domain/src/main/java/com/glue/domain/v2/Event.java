package com.glue.domain.v2;

import java.util.Date;
import java.util.List;

/**
 * Event class.
 * <p>
 * Use the EventOperations class to manipulate
 * 
 * 
 * 
 * 
 */
public class Event {

    /**
     * Event ID.
     */

    private String id;

    /**
     * Event title
     */
    private String title;

    /**
     * Event URL
     */
    private String url;

    /**
     * Description
     * 
     */
    private String description;

    /**
     * Event start time
     */

    protected Date startTime;

    /**
     * Event end time. Not all events have end times
     */
    private Date stopTime;

    /**
     * Timezone of the event in Olson format
     */

    private String olsonPath;

    /**
     * Whether the event is an all-day event
     */

    private boolean allDay = false;

    /**
     * Whether the event is free
     */
    private boolean free = false;

    /**
     * Event price
     */
    private String price;

    /**
     * Whether the event is withdrawn (deleted)
     */
    private boolean withdrawn;

    private String withdrawnNote;

    /**
     * Privacy of the event. "1" is public
     */
    private int privacy = 1;

    /**
     * List of parent events
     */

    private List<Event> parents;

    /**
     * List of child events
     */

    private List<Event> children;

    /**
     * List of links
     */

    private List<Link> links;

    /**
     * Event comments
     */

    private List<Comment> comments;

    /**
     * Event trackbacks
     */

    private List<Trackback> trackbacks;

    /**
     * Performers for the event
     */

    private List<Performer> performers;

    /**
     * MultipleImages
     */

    private List<Image> images;

    /**
     * SingleImage
     */

    private List<Image> singleImageList;

    /**
     * List f tags
     */

    private List<Tag> tags;

    /**
     * Event properties
     */

    private List<Property> properties;

    /**
     * Users watching or going to the event
     */

    private List<User> going;

    private List<Category> categories;

    private Venue venue;

    /**
     * Return the venue from the given information. If a venue object does not
     * yet exist, but an SVID or venue name is present, a venue object will be
     * constructed from this data and returned
     * 
     * @return The Venue Object
     */
    public Venue getVenue() {
	return venue;
    }

    /**
     * Whether the event is all day or not
     * 
     * @return the allDay
     */
    public boolean isAllDay() {
	return allDay;
    }

    /**
     * Whether the event is all day or not
     * 
     * @param allDay
     *            the allDay to set
     */
    public void setAllDay(boolean allDay) {
	this.allDay = allDay;
    }

    /**
     * Event description
     * 
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * Event description
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * The event SEID
     * 
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * The event ID
     * 
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * Event start time
     * 
     * @return the startTime
     */
    public Date getStartTime() {
	return startTime;
    }

    /**
     * Event start time
     * 
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(Date startTime) {
	this.startTime = startTime;
    }

    /**
     * Event stop time
     * 
     * @return the stopTime
     */
    public Date getStopTime() {
	return stopTime;
    }

    /**
     * Event stop time
     * 
     * @param stopTime
     *            the stopTime to set
     */
    public void setStopTime(Date stopTime) {
	this.stopTime = stopTime;
    }

    /**
     * Event title
     * 
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * Event URL
     * 
     * @return the URL
     */
    public String getURL() {
	return url;
    }

    /**
     * Title of the event
     * 
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * Venue for the event
     * 
     * @param venue
     *            the venue to set
     */
    public void setVenue(Venue venue) {
	this.venue = venue;
    }

    /**
     * Event children
     * 
     * @return the children
     */
    public List<Event> getChildren() {
	return children;
    }

    /**
     * Event children
     * 
     * @param children
     *            the children to set
     */
    public void setChildren(List<Event> children) {
	this.children = children;
    }

    /**
     * Whether the event is free or not
     * 
     * @return the free
     */
    public boolean isFree() {
	return free;
    }

    /**
     * @param free
     *            the free to set
     */
    public void setFree(boolean free) {
	this.free = free;
    }

    /**
     * Return parent events
     * 
     * @return the parents
     */
    public List<Event> getParents() {
	return parents;
    }

    /**
     * Set parent events
     * 
     * @param parents
     *            the parents to set
     */
    public void setParents(List<Event> parents) {
	this.parents = parents;
    }

    /**
     * Price of the event
     * 
     * @return the price
     */
    public String getPrice() {
	return price;
    }

    /**
     * Set the event price
     * 
     * @param price
     *            the price to set
     */
    public void setPrice(String price) {
	this.price = price;
    }

    /**
     * Whether the event is withdrawn (or deleted) or not
     * 
     * @return the withdrawn
     */
    public boolean isWithdrawn() {
	return withdrawn;
    }

    /**
     * Delete the event. Use the EventOperations class to manipulate
     * 
     * 
     * @param withdrawn
     *            the withdrawn to set
     */
    public void setWithdrawn(boolean withdrawn) {
	this.withdrawn = withdrawn;
    }

    /**
     * Get the withdrawn note
     * 
     * @return the withdrawnNote
     */
    public String getWithdrawnNote() {
	return withdrawnNote;
    }

    /**
     * Set the withdrawn note. Use the EventOperations class to manipulate
     * 
     * 
     * @param withdrawnNote
     *            the withdrawnNote to set
     */
    public void setWithdrawnNote(String withdrawnNote) {
	this.withdrawnNote = withdrawnNote;
    }

    /**
     * List of event links
     * 
     * @return the links
     */
    public List<Link> getLinks() {
	return links;
    }

    /**
     * Set event links. Use the EventOperations class to manipulate
     * 
     * 
     * @param links
     *            the links to set
     */
    public void setLinks(List<Link> links) {
	this.links = links;
    }

    /**
     * Return event comments
     * 
     * @return the comments
     */
    public List<Comment> getComments() {
	return comments;
    }

    /**
     * Set the event comments. Use the EventOperations class to manipulate
     * 
     * 
     * Comment)
     * 
     * @param comments
     *            the comments to set
     */
    public void setComments(List<Comment> comments) {
	this.comments = comments;
    }

    /**
     * Get the list of performers for the event
     * 
     * @return the performers
     */
    public List<Performer> getPerformers() {
	return performers;
    }

    /**
     * Set the list of event performers. Use the EventOperations class to
     * manipulate
     * 
     * 
     * Performer)
     * 
     * @param performers
     *            the performers to set
     */
    public void setPerformers(List<Performer> performers) {
	this.performers = performers;
    }

    /**
     * Trackback URLs to the event
     * 
     * @return the trackbacks
     */
    public List<Trackback> getTrackbacks() {
	return trackbacks;
    }

    /**
     * Set the trackback URLs.
     * 
     * @param trackbacks
     *            the trackbacks to set
     */
    public void setTrackbacks(List<Trackback> trackbacks) {
	this.trackbacks = trackbacks;
    }

    /**
     * List of images for the event
     * 
     * @return the images
     */
    public List<Image> getImages() {
	if (singleImageList != null) {
	    return singleImageList;
	} else {
	    return images;
	}
    }

    /**
     * List of images to set. Use the EventOperations class to manipulate
     * 
     * 
     * @param images
     *            the images to set
     */
    public void setImages(List<Image> images) {
	this.images = images;
    }

    /**
     * Return the list of event tags
     * 
     * @return the tags
     */
    public List<Tag> getTags() {
	return tags;
    }

    /**
     * Set the list of event tags. Use the EventOperations class to manipulate
     * 
     * 
     * @param tags
     *            the tags to set
     */
    public void setTags(List<Tag> tags) {
	this.tags = tags;
    }

    /**
     * A list of event properites
     * 
     * @return the properties
     */
    public List<Property> getProperties() {
	return properties;
    }

    /**
     * Set the event properties. Use the EventOperations class to manipulate
     * 
     * 
     * Property)
     * 
     * @param properties
     *            the properties to set
     */
    public void setProperties(List<Property> properties) {
	this.properties = properties;
    }

    /**
     * A list of event categories
     * 
     * @return the categories
     */
    public List<Category> getCategories() {
	return categories;
    }

    /**
     * Set the event categories. Use the EventOperations class to manipulate
     * 
     * 
     * @param categories
     *            the categories to set
     */
    public void setCategories(List<Category> categories) {
	this.categories = categories;
    }

    /**
     * List of users going to the event
     * 
     * @return the going
     */
    public List<User> getGoing() {
	return going;
    }

    /**
     * Set the users going to the event. Use the UserOperations class to
     * manipulate
     * 
     * @param going
     *            the going to set
     */
    public void setGoing(List<User> going) {
	this.going = going;
    }

    /**
     * Event privacy. 1 is Public, 2 is Private
     * 
     * @return the privacy
     */
    public int getPrivacy() {
	return privacy;
    }

    /**
     * Set the event privacy. 1 is Public, 2 is Private
     * 
     * @param privacy
     *            the privacy to set
     */
    public void setPrivacy(int privacy) {
	this.privacy = privacy;
    }

    /**
     * The timezone olson path for the event
     * 
     * @return the olsonPath
     */
    public String getOlsonPath() {
	return olsonPath;
    }

    /**
     * The timezone olson path for the event
     * 
     * @param olsonPath
     *            the olsonPath to set
     */
    public void setOlsonPath(String olsonPath) {
	this.olsonPath = olsonPath;
    }
}
