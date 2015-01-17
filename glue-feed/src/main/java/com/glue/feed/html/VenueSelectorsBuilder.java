package com.glue.feed.html;

public class VenueSelectorsBuilder {

    private VenueSelectors details;

    protected VenueSelectorsBuilder() {
    }

    /**
     * Create a EventDetailsPageBuilder with which to define a
     * <code>EventDetailsPage</code>.
     * 
     * @return a new EventDetailsPageBuilder
     */
    public static VenueSelectorsBuilder newVenueSelectors() {
	VenueSelectorsBuilder builder = new VenueSelectorsBuilder();
	builder.details = new VenueSelectors();
	return builder;
    }

    public VenueSelectorsBuilder withRootBlock(String cssQuery) {
	details.setRootBlock(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder withName(String cssQuery) {
	details.setVenueName(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder withAddress(String cssQuery) {
	details.setVenueAddress(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder withCity(String cssQuery) {
	details.setCity(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder withPhoneNumber(String cssQuery) {
	details.setPhoneNumber(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder withWebsite(String cssQuery) {
	details.setWebsite(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder withDescription(String cssQuery) {
	details.setDescription(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder withThumbnail(String cssQuery) {
	details.setThumbnail(cssQuery);
	return this;
    }

    public VenueSelectors build() {
	return details;
    }

}
