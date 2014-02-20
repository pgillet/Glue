package com.glue.feed.html;

import java.util.Locale;


public class EventDetailsPageBuilder {

    private EventDetailsPage details;

    protected EventDetailsPageBuilder() {
    }

    /**
     * Create a EventDetailsPageBuilder with which to define a
     * <code>EventDetailsPage</code>.
     * 
     * @return a new EventDetailsPageBuilder
     */
    public static EventDetailsPageBuilder newDetails() {
	EventDetailsPageBuilder builder = new EventDetailsPageBuilder();
	builder.details = new EventDetailsPage();
	return builder;
    }

    public EventDetailsPageBuilder withRootBlock(String cssQuery) {
	details.setRootBlock(cssQuery);
	return this;
    }

    public EventDetailsPageBuilder withTitle(String cssQuery) {
	details.setTitle(cssQuery);
	return this;
    }

    public EventDetailsPageBuilder withDescription(String cssQuery) {
	details.setDescription(cssQuery);
	return this;
    }

    public EventDetailsPageBuilder withPerformer(String cssQuery) {
	details.setPerformer(cssQuery);
	return this;
    }

    public EventDetailsPageBuilder withStartDate(String cssQuery) {
	details.setStartDate(cssQuery);
	return this;
    }

    public EventDetailsPageBuilder withEndDate(String cssQuery) {
	details.setEndDate(cssQuery);
	return this;
    }

    public EventDetailsPageBuilder withDatePattern(String cssQuery) {
	details.setDatePattern(cssQuery);
	return this;
    }

    public EventDetailsPageBuilder withThumbnail(String cssQuery) {
	details.setThumbnail(cssQuery);
	return this;
    }

    public EventDetailsPageBuilder withPrice(String cssQuery) {
	details.setPrice(cssQuery);
	return this;
    }

    public EventDetailsPageBuilder withAudience(String cssQuery) {
	details.setAudience(cssQuery);
	return this;
    }

    public EventDetailsPageBuilder withEventType(String cssQuery) {
	details.setEventType(cssQuery);
	return this;
    }

    public EventDetailsPageBuilder withVenueName(String cssQuery) {
	details.setVenueName(cssQuery);
	return this;
    }

    public EventDetailsPageBuilder withVenueAddress(String cssQuery) {
	details.setVenueAddress(cssQuery);
	return this;
    }

    public EventDetailsPageBuilder withLocale(Locale locale) {
	details.setLocale(locale);
	return this;
    }

    public EventDetailsPage build() {
	return details;
    }
}
