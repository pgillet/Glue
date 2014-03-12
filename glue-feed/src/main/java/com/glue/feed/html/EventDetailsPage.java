package com.glue.feed.html;

import java.util.Locale;

public class EventDetailsPage {

    private String rootBlock;
    private String title;
    private String description;
    private String performer;
    private String startDate;
    private String endDate;
    private String datePattern;
    private String thumbnail;
    private String price;
    private String audience;
    private String eventType;
    private String venueName;
    private String venueAddress;
    private Locale locale = Locale.ENGLISH;

    public String getRootBlock() {
	return rootBlock;
    }

    public void setRootBlock(String rootBlock) {
	this.rootBlock = rootBlock;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getPerformer() {
	return performer;
    }

    public void setPerformer(String performer) {
	this.performer = performer;
    }

    public String getStartDate() {
	return startDate;
    }

    public void setStartTime(String startDate) {
	this.startDate = startDate;
    }

    public String getEndDate() {
	return endDate;
    }

    public void setEndDate(String endDate) {
	this.endDate = endDate;
    }

    public String getDatePattern() {
	return datePattern;
    }

    public void setDatePattern(String datePattern) {
	this.datePattern = datePattern;
    }

    public String getThumbnail() {
	return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
	this.thumbnail = thumbnail;
    }

    public String getPrice() {
	return price;
    }

    public void setPrice(String price) {
	this.price = price;
    }

    public String getAudience() {
	return audience;
    }

    public void setAudience(String audience) {
	this.audience = audience;
    }

    public String getEventType() {
	return eventType;
    }

    public void setEventType(String eventType) {
	this.eventType = eventType;
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

    public Locale getLocale() {
	return locale;
    }

    public void setLocale(Locale locale) {
	this.locale = locale;
    }
}
