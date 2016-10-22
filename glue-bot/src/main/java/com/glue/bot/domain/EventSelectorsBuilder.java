package com.glue.bot.domain;

import java.util.Locale;


public class EventSelectorsBuilder {

    private EventSelectors selectors;

    protected EventSelectorsBuilder() {
    }

    /**
     * Create a EventSelectorsBuilder with which to define a
     * <code>EventSelectors</code>.
     * 
     * @return a new EventSelectorsBuilder
     */
    public static EventSelectorsBuilder newEventSelectors() {
	EventSelectorsBuilder builder = new EventSelectorsBuilder();
	builder.selectors = new EventSelectors();
	return builder;
    }

    public EventSelectorsBuilder rootBlock(String cssQuery) {
	selectors.setRootBlock(cssQuery);
	return this;
    }

    public EventSelectorsBuilder title(String cssQuery) {
	selectors.setTitle(cssQuery);
	return this;
    }

    public EventSelectorsBuilder description(String cssQuery) {
	selectors.setDescription(cssQuery);
	return this;
    }

    public EventSelectorsBuilder dates(String cssQuery) {
	selectors.setDates(cssQuery);
	return this;
    }

    public EventSelectorsBuilder datePattern(String cssQuery) {
	selectors.setDatePattern(cssQuery);
	return this;
    }

    public EventSelectorsBuilder thumbnail(String cssQuery) {
	selectors.setThumbnail(cssQuery);
	return this;
    }

    public EventSelectorsBuilder price(String cssQuery) {
	selectors.setPrice(cssQuery);
	return this;
    }

    public EventSelectorsBuilder eventType(String cssQuery) {
	selectors.setEventType(cssQuery);
	return this;
    }

    public EventSelectorsBuilder locale(Locale locale) {
	selectors.setLocale(locale);
	return this;
    }

    public EventSelectorsBuilder venueSelectors(
	    VenueSelectors venueSelectors) {
	selectors.setVenueSelectors(venueSelectors);
	return this;
    }

    public EventSelectorsBuilder venueLink(String cssQuery) {
	selectors.setVenueRootBlock(cssQuery);
	return this;
    }

    public EventSelectors build() {
	return selectors;
    }
}
