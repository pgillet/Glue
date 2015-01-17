package com.glue.feed.html;

import java.util.Locale;


public class EventSelectorsBuilder {

    private EventSelectors selectors;

    protected EventSelectorsBuilder() {
    }

    /**
     * Create a EventDetailsPageBuilder with which to define a
     * <code>EventDetailsPage</code>.
     * 
     * @return a new EventDetailsPageBuilder
     */
    public static EventSelectorsBuilder newEventSelectors() {
	EventSelectorsBuilder builder = new EventSelectorsBuilder();
	builder.selectors = new EventSelectors();
	return builder;
    }

    public EventSelectorsBuilder withRootBlock(String cssQuery) {
	selectors.setRootBlock(cssQuery);
	return this;
    }

    public EventSelectorsBuilder withTitle(String cssQuery) {
	selectors.setTitle(cssQuery);
	return this;
    }

    public EventSelectorsBuilder withDescription(String cssQuery) {
	selectors.setDescription(cssQuery);
	return this;
    }

    public EventSelectorsBuilder withPerformer(String cssQuery) {
	selectors.setPerformer(cssQuery);
	return this;
    }

    public EventSelectorsBuilder withDates(String cssQuery) {
	selectors.setDates(cssQuery);
	return this;
    }

    public EventSelectorsBuilder withDatePattern(String cssQuery) {
	selectors.setDatePattern(cssQuery);
	return this;
    }

    public EventSelectorsBuilder withThumbnail(String cssQuery) {
	selectors.setThumbnail(cssQuery);
	return this;
    }

    public EventSelectorsBuilder withPrice(String cssQuery) {
	selectors.setPrice(cssQuery);
	return this;
    }

    public EventSelectorsBuilder withAudience(String cssQuery) {
	selectors.setAudience(cssQuery);
	return this;
    }

    public EventSelectorsBuilder withEventType(String cssQuery) {
	selectors.setEventType(cssQuery);
	return this;
    }

    public EventSelectorsBuilder withLocale(Locale locale) {
	selectors.setLocale(locale);
	return this;
    }

    public EventSelectorsBuilder withVenueSelectors(
	    VenueSelectors venueSelectors) {
	selectors.setVenueSelectors(venueSelectors);
	return this;
    }

    public EventSelectorsBuilder withVenueLink(String cssQuery) {
	selectors.setVenueLink(cssQuery);
	return this;
    }

    public EventSelectors build() {
	return selectors;
    }
}
