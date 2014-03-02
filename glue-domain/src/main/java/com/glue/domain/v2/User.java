package com.glue.domain.v2;

import java.util.Date;
import java.util.List;

/**
 *  user object
 * 
 * 
 * 
 */

public class User {

    private String id;

    /**
     * Username
     */
    private String username;

    /**
     * Only used by event methods to return the status of user for the event,
     * whether the user is watching or going
     */

    private String eventWatchingGoingStatus;

    /**
     * User bio
     */
    private String bio;

    /**
     * User hometown
     */
    private String hometown;

    /**
     * First Name
     */

    private String firstName;

    /**
     * Last name
     */

    private String lastName;

    /**
     * Interests
     */
    private String interests;

    /**
     * Registration date
     */
    private Date registrationDate;

    private String reputation;

    /**
     * Links about the user
     */

    private List<Link> links;

    /**
     * User images
     */

    private List<Image> images;

    /**
     * List of events the user is either watching or going
     */

    private List<Event> going;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    /**
     * User bio
     * 
     * @return the bio
     */
    public String getBio() {
	return bio;
    }

    /**
     * Set the user bio
     * 
     * @param bio
     *            the bio to set
     */
    public void setBio(String bio) {
	this.bio = bio;
    }

    /**
     * Get the users watching going status for an event
     * 
     * @return the eventWatchingGoingStatus
     */
    public String getEventWatchingGoingStatus() {
	return eventWatchingGoingStatus;
    }

    /**
     * For events, get the users watching status
     * 
     * @param eventWatchingGoingStatus
     *            the eventWatchingGoingStatus to set
     */
    public void setEventWatchingGoingStatus(String eventWatchingGoingStatus) {
	this.eventWatchingGoingStatus = eventWatchingGoingStatus;
    }

    /**
     * First name
     * 
     * @return the firstName
     */
    public String getFirstName() {
	return firstName;
    }

    /**
     * First name
     * 
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     * List of events the user is going
     * 
     * @return the going
     */
    public List<Event> getGoing() {
	return going;
    }

    /**
     * List of events the user is going
     * 
     * @param going
     *            the going to set
     */
    public void setGoing(List<Event> going) {
	this.going = going;
    }

    /**
     * Users hometown
     * 
     * @return the hometown
     */
    public String getHometown() {
	return hometown;
    }

    /**
     * Users hometown
     * 
     * @param hometown
     *            the hometown to set
     */
    public void setHometown(String hometown) {
	this.hometown = hometown;
    }

    /**
     * User images
     * 
     * @return the images
     */
    public List<Image> getImages() {
	return images;
    }

    /**
     * User images
     * 
     * @param images
     *            the images to set
     */
    public void setImages(List<Image> images) {
	this.images = images;
    }

    /**
     * User interests
     * 
     * @return the interests
     */
    public String getInterests() {
	return interests;
    }

    /**
     * Set user interests
     * 
     * @param interests
     *            the interests to set
     */
    public void setInterests(String interests) {
	this.interests = interests;
    }

    /**
     * User last name
     * 
     * @return the lastName
     */
    public String getLastName() {
	return lastName;
    }

    /**
     * User last name
     * 
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    /**
     * List of links
     * 
     * @return the links
     */
    public List<Link> getLinks() {
	return links;
    }

    /**
     * Set the list of links
     * 
     * @param links
     *            the links to set
     */
    public void setLinks(List<Link> links) {
	this.links = links;
    }

    /**
     * User registration date
     * 
     * @return the registrationDate
     */
    public Date getRegistrationDate() {
	return registrationDate;
    }

    /**
     * User registration date
     * 
     * @param registrationDate
     *            the registrationDate to set
     */
    public void setRegistrationDate(Date registrationDate) {
	this.registrationDate = registrationDate;
    }

    /**
     * User reputation
     * 
     * @return the reputation
     */
    public String getReputation() {
	return reputation;
    }

    /**
     * User reputation
     * 
     * @param reputation
     *            the reputation to set
     */
    public void setReputation(String reputation) {
	this.reputation = reputation;
    }

    /**
     * User name
     * 
     * @return the username
     */
    public String getUsername() {
	return username;
    }

    /**
     * Set the username. Note that this field cannot be changed
     * 
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
	this.username = username;
    }

}
