package com.glue.bot.domain;


public class VenueSelectorsBuilder {

    private VenueSelectors details;

    protected VenueSelectorsBuilder() {
    }

    /**
     * Create a VenueSelectorsBuilder with which to define a
     * <code>VenueSelectors</code>.
     * 
     * @return a new VenueSelectorsBuilder
     */
    public static VenueSelectorsBuilder newVenueSelectors() {
	VenueSelectorsBuilder builder = new VenueSelectorsBuilder();
	builder.details = new VenueSelectors();
	return builder;
    }

    public VenueSelectorsBuilder rootBlock(String cssQuery) {
	details.setRootBlock(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder name(String cssQuery) {
	details.setVenueName(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder address(String cssQuery) {
	details.setVenueAddress(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder city(String cssQuery) {
	details.setCity(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder phoneNumber(String cssQuery) {
	details.setPhoneNumber(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder website(String cssQuery) {
	details.setWebsite(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder description(String cssQuery) {
	details.setDescription(cssQuery);
	return this;
    }

    public VenueSelectorsBuilder thumbnail(String cssQuery) {
	details.setThumbnail(cssQuery);
	return this;
    }

    public VenueSelectors build() {
	return details;
    }

}
