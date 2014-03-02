package com.glue.domain.v2;

import java.util.List;

/**
 * Venue object
 * 
 * 
 * 
 */
public class Venue {

    private String id;

    private String name;

    private String description;

    private String type;

    private int display;

    private String address;

    private String city;

    private String region;

    private String regionAbbreviation;

    private String postalCode;

    private String country;

    private String countryTwoLetterAbbreviation;

    private String countryThreeLetterAbbreviation;

    private double latitude;

    private double longitude;

    private int privacy = 1;

    private String url;

    private String urlType;

    private String geocodeType;

    private List<Venue> parents;

    private List<Venue> children;

    private List<Event> events;

    private List<Link> links;

    private List<Comment> comments;

    private List<Trackback> trackbacks;

    private List<Property> properties;

    private List<Tag> tags;

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * Street address of the venue
     * 
     * @return the venueAddress
     */
    public String getAddress() {
	return address;
    }

    /**
     * Street address of the venue
     * 
     * @param venueAddress
     *            the venueAddress to set
     */
    public void setAddress(String venueAddress) {
	this.address = venueAddress;
    }

    /**
     * City of the venue
     * 
     * @return the venueCity
     */
    public String getCity() {
	return city;
    }

    /**
     * Set the city
     * 
     * @param venueCity
     *            the venueCity to set
     */
    public void setCity(String venueCity) {
	this.city = venueCity;
    }

    /**
     * Set the display property
     * 
     * @return the venueDisplay
     */
    public int getDisplay() {
	return display;
    }

    /**
     * Set the display property
     * 
     * @param venueDisplay
     *            the venueDisplay to set
     */
    public void setDisplay(int venueDisplay) {
	this.display = venueDisplay;
    }

    /**
     * Name of the venue
     * 
     * @return the venueName
     */
    public String getName() {
	return name;
    }

    /**
     * Set the venue name
     * 
     * @param venueName
     *            the venueName to set
     */
    public void setName(String venueName) {
	this.name = venueName;
    }

    /**
     * Type of venue
     * 
     * @return the venueType
     */
    public String getType() {
	return type;
    }

    /**
     * Set the venue type
     * 
     * @param venueType
     *            the venueType to set
     */
    public void setType(String venueType) {
	this.type = venueType;
    }

    /**
     * Country the venue is in
     * 
     * @return the country
     */
    public String getCountry() {
	return country;
    }

    /**
     * Country the venue is in
     * 
     * @param country
     *            the country to set
     */
    public void setCountry(String country) {
	this.country = country;
    }

    /**
     * Three letter country abbreviation
     * 
     * @return the countryThreeLetterAbbreviation
     */
    public String getCountryThreeLetterAbbreviation() {
	return countryThreeLetterAbbreviation;
    }

    /**
     * Three letter country abbreviation
     * 
     * @param countryThreeLetterAbbreviation
     *            the countryThreeLetterAbbreviation to set
     */
    public void setCountryThreeLetterAbbreviation(
	    String countryThreeLetterAbbreviation) {
	this.countryThreeLetterAbbreviation = countryThreeLetterAbbreviation;
    }

    /**
     * 2-letter country abbreviation
     * 
     * @return the countryTwoLetterAbbreviation
     */
    public String getCountryTwoLetterAbbreviation() {
	return countryTwoLetterAbbreviation;
    }

    /**
     * 2-letter abbreviation
     * 
     * @param countryTwoLetterAbbreviation
     *            the countryTwoLetterAbbreviation to set
     */
    public void setCountryTwoLetterAbbreviation(
	    String countryTwoLetterAbbreviation) {
	this.countryTwoLetterAbbreviation = countryTwoLetterAbbreviation;
    }

    /**
     * Geocode type for the venue
     * 
     * @return the geocodeType
     */
    public String getGeocodeType() {
	return geocodeType;
    }

    /**
     * Geocode type of the venue. Note: this property should not be set by
     * external users
     * 
     * @param geocodeType
     *            the geocodeType to set
     */
    public void setGeocodeType(String geocodeType) {
	this.geocodeType = geocodeType;
    }

    /**
     * Latitude
     * 
     * @return the latitude
     */
    public double getLatitude() {
	return latitude;
    }

    /**
     * Latitude
     * 
     * @param latitude
     *            the latitude to set
     */
    public void setLatitude(double latitude) {
	this.latitude = latitude;
    }

    /**
     * Longitude
     * 
     * @return the longitude
     */
    public double getLongitude() {
	return longitude;
    }

    /**
     * Longitude
     * 
     * @param longitude
     *            the longitude to set
     */
    public void setLongitude(double longitude) {
	this.longitude = longitude;
    }

    /**
     * Venue postal code
     * 
     * @return the postalCode
     */
    public String getPostalCode() {
	return postalCode;
    }

    /**
     * Postal code
     * 
     * @param postalCode
     *            the postalCode to set
     */
    public void setPostalCode(String postalCode) {
	this.postalCode = postalCode;
    }

    /**
     * Region of the venue
     * 
     * @return the region
     */
    public String getRegion() {
	return region;
    }

    /**
     * Venue Region
     * 
     * @param region
     *            the region to set
     */
    public void setRegion(String region) {
	this.region = region;
    }

    /**
     * Region abbreviation
     * 
     * @return the regionAbbreviation
     */
    public String getRegionAbbreviation() {
	return regionAbbreviation;
    }

    /**
     * Region abbreviation
     * 
     * @param regionAbbreviation
     *            the regionAbbreviation to set
     */
    public void setRegionAbbreviation(String regionAbbreviation) {
	this.regionAbbreviation = regionAbbreviation;
    }

    /**
     * List of child venues
     * 
     * @return the children
     */
    public List<Venue> getChildren() {
	return children;
    }

    /**
     * Set the child venues
     * 
     * @param children
     *            the children to set
     */
    public void setChildren(List<Venue> children) {
	this.children = children;
    }

    /**
     * Venue comments
     * 
     * @return the comments
     */
    public List<Comment> getComments() {
	return comments;
    }

    /**
     * Set the venue comments
     * 
     * @param comments
     *            the comments to set
     */
    public void setComments(List<Comment> comments) {
	this.comments = comments;
    }

    /**
     * List of events at the venue
     * 
     * @return the events
     */
    public List<Event> getEvents() {
	return events;
    }

    /**
     * Set the venue event list
     * 
     * @param events
     *            the events to set
     */
    public void setEvents(List<Event> events) {
	this.events = events;
    }

    /**
     * List of web links
     * 
     * @return the links
     */
    public List<Link> getLinks() {
	return links;
    }

    /**
     * List of web links
     * 
     * @param links
     *            the links to set
     */
    public void setLinks(List<Link> links) {
	this.links = links;
    }

    /**
     * Parent venues
     * 
     * @return the parents
     */
    public List<Venue> getParents() {
	return parents;
    }

    /**
     * Set the parent venues
     * 
     * @param parents
     *            the parents to set
     */
    public void setParents(List<Venue> parents) {
	this.parents = parents;
    }

    /**
     * List of venue trackbacks
     * 
     * @return the trackbacks
     */
    public List<Trackback> getTrackbacks() {
	return trackbacks;
    }

    /**
     * List of venue trackbacks
     * 
     * @param trackbacks
     *            the trackbacks to set
     */
    public void setTrackbacks(List<Trackback> trackbacks) {
	this.trackbacks = trackbacks;
    }

    /**
     * Venue properties
     * 
     * @return the properties
     */
    public List<Property> getProperties() {
	return properties;
    }

    /**
     * List of venue properties
     * 
     * @param properties
     *            the properties to set
     */
    public void setProperties(List<Property> properties) {
	this.properties = properties;
    }

    /**
     * Venue description
     * 
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * Set the venue description
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Venue privacy
     * 
     * @return the privacy
     */
    public int getPrivacy() {
	return privacy;
    }

    /**
     * Set the venue privacy
     * 
     * @param privacy
     *            the privacy to set
     */
    public void setPrivacy(int privacy) {
	this.privacy = privacy;
    }

    /**
     * Venue URL
     * 
     * @return the url
     */
    public String getUrl() {
	return url;
    }

    /**
     * Set the venue URL
     * 
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
	this.url = url;
    }

    /**
     * URL type
     * 
     * @return the urlType
     */
    public String getUrlType() {
	return urlType;
    }

    /**
     * URL type
     * 
     * @param urlType
     *            the urlType to set
     */
    public void setUrlType(String urlType) {
	this.urlType = urlType;
    }

    /**
     * List of venue tags
     * 
     * @return the tags
     */
    public List<Tag> getTags() {
	return tags;
    }

    /**
     * Set the venue tags
     * 
     * @param tags
     *            the tags to set
     */
    public void setTags(List<Tag> tags) {
	this.tags = tags;
    }

}