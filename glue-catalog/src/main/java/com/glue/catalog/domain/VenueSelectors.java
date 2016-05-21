package com.glue.catalog.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VenueSelectors {

    private String rootBlock;
    private String venueName;
    private String venueAddress;
    private String city;
    private String phoneNumber;
    private String website;
    private String description;
    private String thumbnail;

    public String getRootBlock() {
	return rootBlock;
    }

    public void setRootBlock(String rootBlock) {
	this.rootBlock = rootBlock;
    }

    public String getVenueName() {
	return venueName;
    }

    public void setVenueName(String venueName) {
	this.venueName = venueName;
    }

    public String getVenueAddress() {
	return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
	this.venueAddress = venueAddress;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getPhoneNumber() {
	return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
	return website;
    }

    public void setWebsite(String website) {
	this.website = website;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getThumbnail() {
	return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
	this.thumbnail = thumbnail;
    }

}
