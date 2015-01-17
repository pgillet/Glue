package com.glue.feed.html;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.glue.domain.Image;
import com.glue.domain.ImageItem;
import com.glue.domain.Venue;

public class VenueMappingStrategy implements HTMLMappingStrategy<Venue> {

    private HTMLFetcher hf = new HTMLFetcher();
    private VenueSelectors selectors;
    private Venue venueRef = new Venue();

    public VenueMappingStrategy(VenueSelectors selectors) {
	this(selectors, null);
    }

    public VenueMappingStrategy(VenueSelectors selectors, Venue venueRef) {
	this.selectors = selectors;
	this.venueRef = venueRef;
    }

    public Venue getVenueRef() {
	return venueRef;
    }

    public void setVenueRef(Venue venueRef) {
	this.venueRef = venueRef;
    }

    @Override
    public Venue parse(String url) throws Exception {

	Document doc = hf.fetch(url);
	return parse(doc);
    }

    protected Venue parse(Element doc) throws Exception {

	ElementDecorator elem = new ElementDecorator(doc);

	ElementDecorator otherelem = elem.selectFirst(selectors.getRootBlock());
	elem = (otherelem != null ? otherelem : elem);

	Venue venue = getRefCopy();

	venue.setName(elem.selectText(selectors.getVenueName(), venue.getName()));
	venue.setAddress(elem.selectText(selectors.getVenueAddress(),
		venue.getAddress()));
	venue.setCity(elem.selectText(selectors.getCity(), venue.getCity()));
	venue.setUrl(elem.selectText(selectors.getWebsite(), venue.getUrl()));
	venue.setDescription(elem.selectText(selectors.getDescription(),
		venue.getDescription()));

	// details.getPhoneNumber();

	// Images
	String thumbnailQuery = selectors.getThumbnail();
	if (thumbnailQuery != null) {
	    Elements elems = elem.getElement().select(thumbnailQuery);
	    // Get media
	    elems = elems.select("[src]");

	    if (!elems.isEmpty()) {

		String imageUrl = elems.attr("abs:src");

		ImageItem item = new ImageItem();
		item.setUrl(imageUrl);

		Image image = new Image();
		image.setOriginal(item);
		image.setUrl(imageUrl);
		image.setSource(doc.baseUri());
		image.setSticky(true);

		venue.getImages().add(image);
	    }
	}

	return venue;
    }

    /**
     * Returns a dumb copy of the reference event.
     * 
     * @return
     */
    private Venue getRefCopy() {
	Venue copy = new Venue();

	copy.setAddress(venueRef.getAddress());
	copy.setChildren(venueRef.getChildren());
	copy.setCity(venueRef.getCity());
	copy.setComments(venueRef.getComments());
	copy.setCountry(venueRef.getCountry());
	copy.setCountryThreeLetterAbbreviation(venueRef
		.getCountryThreeLetterAbbreviation());
	copy.setCountryTwoLetterAbbreviation(venueRef
		.getCountryTwoLetterAbbreviation());
	copy.setCreated(venueRef.getCreated());
	copy.setDescription(venueRef.getDescription());
	copy.setEvents(venueRef.getEvents());
	copy.setGeocodeType(venueRef.getGeocodeType());
	copy.setId(venueRef.getId());
	copy.setImages(venueRef.getImages());
	copy.setLatitude(venueRef.getLatitude());
	copy.setLongitude(venueRef.getLongitude());
	copy.setLinks(venueRef.getLinks());
	copy.setName(venueRef.getName());
	copy.setParent(venueRef.getParent());
	copy.setPostalCode(venueRef.getPostalCode());
	copy.setProperties(venueRef.getProperties());
	copy.setReference(venueRef.isReference());
	copy.setRegion(venueRef.getRegion());
	copy.setRegionAbbreviation(venueRef.getRegionAbbreviation());
	copy.setSource(venueRef.getSource());
	copy.setTags(venueRef.getTags());
	copy.setTimeZone(venueRef.getTimeZone());
	copy.setType(venueRef.getType());
	copy.setUrl(venueRef.getUrl());

	return copy;
    }

}
