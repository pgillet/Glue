package com.glue.bot;

import java.util.Locale;

public class EventSelectors {

    private String rootBlock;
    private String title;
    private String description;
    private String performer;
    private String dates;
    private String datePattern;
    private String thumbnail;
    private String price;
    private String audience;
    private String eventType;
    private String venueRootBlock;
    private VenueSelectors venueSelectors;
    private Locale locale = Locale.FRENCH;

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

    public String getDates() {
	return dates;
    }

    public void setDates(String dates) {
	this.dates = dates;
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

    public String getVenueRootBlock() {
	return venueRootBlock;
    }

    public void setVenueRootBlock(String venueRootBlock) {
	this.venueRootBlock = venueRootBlock;
    }

    public VenueSelectors getVenueSelectors() {
	return venueSelectors;
    }

    public void setVenueSelectors(VenueSelectors venueDetails) {
	this.venueSelectors = venueDetails;
    }

    public Locale getLocale() {
	return locale;
    }

    public void setLocale(Locale locale) {
	this.locale = locale;
    }
}
