package com.glue.domain.v2;

import java.util.Date;
import java.util.List;

/**
 * Event class.
 */
public class Event {

    /**
     * Event ID.
     */
    private String id;

    /**
     * Event title.
     */
    private String title;

    /**
     * Description.
     */
    private String description;

    /**
     * Event URL.
     */
    private String url;

    /**
     * Event start time.
     */
    protected Date startTime;

    /**
     * Event end time. Not all events have end times.
     */
    private Date stopTime;

    /**
     * Timezone of the event.
     */
    private String timeZone;

    /**
     * Whether the event is an all-day event.
     */
    private boolean allDay = false;

    /**
     * Whether the event is free.
     */
    private boolean free = false;

    /**
     * Event price.
     */
    private String price;

    /**
     * Whether the event is withdrawn (deleted).
     */
    private boolean withdrawn;

    private String withdrawnNote;

    /**
     * Parent event.
     */
    private Event parent;

    /**
     * List of child events.
     */
    private List<Event> children;

    /**
     * List of links.
     */
    private List<Link> links;

    /**
     * Event comments.
     */
    private List<Comment> comments;

    /**
     * Performers for the event.
     */
    private List<Performer> performers;

    /**
     * Images.
     */
    private List<Image> images;

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

    private boolean reference;

    private Venue venue;

    /**
     * Date event was created.
     */
    private Date created;

    /**
     * Return the venue from the given information.
     * 
     * @return The Venue Object
     */
    public Venue getVenue() {
	return venue;
    }

    /**
     * Whether the event is all day or not.
     * 
     * @return the allDay
     */
    public boolean isAllDay() {
	return allDay;
    }

    /**
     * Whether the event is all day or not.
     * 
     * @param allDay
     *            the allDay to set
     */
    public void setAllDay(boolean allDay) {
	this.allDay = allDay;
    }

    /**
     * Event description.
     * 
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * Event description.
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * The event ID.
     * 
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * The event ID.
     * 
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * Event start time.
     * 
     * @return the startTime
     */
    public Date getStartTime() {
	return startTime;
    }

    /**
     * Event start time.
     * 
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(Date startTime) {
	this.startTime = startTime;
    }

    /**
     * Event stop time.
     * 
     * @return the stopTime
     */
    public Date getStopTime() {
	return stopTime;
    }

    /**
     * Event stop time.
     * 
     * @param stopTime
     *            the stopTime to set
     */
    public void setStopTime(Date stopTime) {
	this.stopTime = stopTime;
    }

    /**
     * Event title.
     * 
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * Event URL.
     * 
     * @return the URL
     */
    public String getURL() {
	return url;
    }

    /**
     * Title of the event.
     * 
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * Venue for the event.
     * 
     * @param venue
     *            the venue to set
     */
    public void setVenue(Venue venue) {
	this.venue = venue;
    }

    /**
     * Event children.
     * 
     * @return the children
     */
    public List<Event> getChildren() {
	return children;
    }

    /**
     * Event children.
     * 
     * @param children
     *            the children to set
     */
    public void setChildren(List<Event> children) {
	this.children = children;
    }

    /**
     * Whether the event is free or not.
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
     * Return the parent event.
     * 
     * @return the parent
     */
    public Event getParent() {
	return parent;
    }

    /**
     * Set the parent event.
     * 
     * @param parent
     *            the parent to set
     */
    public void setParent(Event parent) {
	this.parent = parent;
    }

    /**
     * Price of the event.
     * 
     * @return the price
     */
    public String getPrice() {
	return price;
    }

    /**
     * Set the event price.
     * 
     * @param price
     *            the price to set
     */
    public void setPrice(String price) {
	this.price = price;
    }

    /**
     * Whether the event is withdrawn (or deleted) or not.
     * 
     * @return the withdrawn
     */
    public boolean isWithdrawn() {
	return withdrawn;
    }

    /**
     * Delete the event.
     * 
     * @param withdrawn
     *            the withdrawn to set
     */
    public void setWithdrawn(boolean withdrawn) {
	this.withdrawn = withdrawn;
    }

    /**
     * Get the withdrawn note.
     * 
     * @return the withdrawnNote
     */
    public String getWithdrawnNote() {
	return withdrawnNote;
    }

    /**
     * Set the withdrawn note.
     * 
     * @param withdrawnNote
     *            the withdrawnNote to set
     */
    public void setWithdrawnNote(String withdrawnNote) {
	this.withdrawnNote = withdrawnNote;
    }

    /**
     * List of event links.
     * 
     * @return the links
     */
    public List<Link> getLinks() {
	return links;
    }

    /**
     * Set event links.
     * 
     * @param links
     *            the links to set
     */
    public void setLinks(List<Link> links) {
	this.links = links;
    }

    /**
     * Return event comments.
     * 
     * @return the comments
     */
    public List<Comment> getComments() {
	return comments;
    }

    /**
     * Set the event comments.
     * 
     * @param comments
     *            the comments to set
     */
    public void setComments(List<Comment> comments) {
	this.comments = comments;
    }

    /**
     * Get the list of performers for the event.
     * 
     * @return the performers
     */
    public List<Performer> getPerformers() {
	return performers;
    }

    /**
     * Set the list of event performers.
     * 
     * @param performers
     *            the performers to set
     */
    public void setPerformers(List<Performer> performers) {
	this.performers = performers;
    }

    /**
     * List of images for the event.
     * 
     * @return the images
     */
    public List<Image> getImages() {
	return images;
    }

    /**
     * List of images to set.
     * 
     * @param images
     *            the images to set
     */
    public void setImages(List<Image> images) {
	this.images = images;
    }

    /**
     * Return the list of event tags.
     * 
     * @return the tags
     */
    public List<Tag> getTags() {
	return tags;
    }

    /**
     * Set the list of event tags.
     * 
     * @param tags
     *            the tags to set
     */
    public void setTags(List<Tag> tags) {
	this.tags = tags;
    }

    /**
     * A list of event properties.
     * 
     * @return the properties
     */
    public List<Property> getProperties() {
	return properties;
    }

    /**
     * Set the event properties.
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
     * Set the event categories.
     * 
     * 
     * @param categories
     *            the categories to set
     */
    public void setCategories(List<Category> categories) {
	this.categories = categories;
    }

    /**
     * List of users going to the event.
     * 
     * @return the going
     */
    public List<User> getGoing() {
	return going;
    }

    /**
     * Set the users going to the event.
     * 
     * @param going
     *            the going to set
     */
    public void setGoing(List<User> going) {
	this.going = going;
    }

    /**
     * The timezone for the event.
     * 
     * @return the timeZone
     */
    public String getTimeZone() {
	return timeZone;
    }

    /**
     * The timezone for the event.
     * 
     * @param timeZone
     *            the timeZone to set
     */
    public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
    }

    /**
     * Date event was created.
     * 
     * @return the created
     */
    public Date getCreated() {
	return created;
    }

    /**
     * Date event was created.
     * 
     * @param created
     *            the created to set
     */
    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     * Whether this event is the reference.
     * 
     * @return the reference
     */
    public boolean isReference() {
	return reference;
    }

    /**
     * Whether this event is the reference.
     * 
     * @param reference
     *            the reference to set
     */
    public void setReference(boolean reference) {
	this.reference = reference;
    }
}
