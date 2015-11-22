package com.glue.feed.html;

import org.apache.commons.lang.StringUtils;
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
    public Venue parse(Element e) throws Exception {

	Element elem = e;
	String location = null;
	Elements elems;

	if ("a".equals(elem.tagName())) {
	    location = elem.attr("abs:href");
	    elem = hf.fetch(location);
	}

	if (selectors.getRootBlock() != null) {
	    Elements tmp = elem.select(selectors.getRootBlock());
	    Validate.single(tmp);
	    elem = tmp.get(0);
	}

	Venue venue = getRefCopy();

	// Venue name

	elems = elem.select(selectors.getVenueName());
	Validate.single(elems);
	String name = elems.text();
	venue.setName(StringUtils.defaultIfBlank(name, venue.getName()));

	// Address
	if (selectors.getVenueAddress() != null) {
	    String address = elem.select(selectors.getVenueAddress()).text();
	    venue.setAddress(StringUtils.defaultIfBlank(address,
		    venue.getAddress()));
	}

	if (selectors.getCity() != null) {
	    String city = elem.select(selectors.getCity()).text();
	    venue.setCity(StringUtils.defaultIfBlank(city, venue.getCity()));
	}

	if (selectors.getWebsite() != null) {
	    String url = elem.select(selectors.getWebsite()).text();
	    venue.setUrl(StringUtils.defaultIfBlank(url, venue.getUrl()));
	}

	if (selectors.getDescription() != null) {
	    String description = elem.select(selectors.getDescription()).text();
	    venue.setDescription(StringUtils.defaultIfBlank(description,
		    venue.getDescription()));
	}

	// details.getPhoneNumber();

	// Images
	String thumbnailQuery = selectors.getThumbnail();
	if (thumbnailQuery != null) {
	    elems = elem.select(thumbnailQuery);
	    // Get media
	    elems = elems.select(HtmlTags.IMAGE);

	    for (Element imgElement : elems) {

		String imageUrl = imgElement.attr("abs:src");

		ImageItem item = new ImageItem();
		item.setUrl(imageUrl);

		Image image = new Image();
		image.setOriginal(item);
		image.setUrl(imageUrl);
		image.setSource(location);
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
