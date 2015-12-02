package com.glue.feed.html;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.glue.domain.Image;
import com.glue.domain.ImageItem;
import com.glue.domain.Venue;

public class VenueMappingStrategy implements HTMLMappingStrategy<Venue> {

    private HTMLFetcher hf = new HTMLFetcher();
    private VenueSelectors selectors;
    private Venue venueTemplate = new Venue();

    public VenueMappingStrategy(VenueSelectors selectors) {
	this(selectors, null);
    }

    public VenueMappingStrategy(VenueSelectors selectors, Venue venueTemplate) {
	this.selectors = selectors;
	this.venueTemplate = venueTemplate;
    }

    public Venue getVenueTemplate() {
	return venueTemplate;
    }

    public void setVenueTemplate(Venue venueTemplate) {
	this.venueTemplate = venueTemplate;
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

	copy.setAddress(venueTemplate.getAddress());
	copy.setChildren(venueTemplate.getChildren());
	copy.setCity(venueTemplate.getCity());
	copy.setComments(venueTemplate.getComments());
	copy.setCountry(venueTemplate.getCountry());
	copy.setCountryThreeLetterAbbreviation(venueTemplate
		.getCountryThreeLetterAbbreviation());
	copy.setCountryTwoLetterAbbreviation(venueTemplate
		.getCountryTwoLetterAbbreviation());
	copy.setCreated(venueTemplate.getCreated());
	copy.setDescription(venueTemplate.getDescription());
	copy.setEvents(venueTemplate.getEvents());
	copy.setGeocodeType(venueTemplate.getGeocodeType());
	copy.setId(venueTemplate.getId());
	copy.setImages(venueTemplate.getImages());
	copy.setLatitude(venueTemplate.getLatitude());
	copy.setLongitude(venueTemplate.getLongitude());
	copy.setLinks(venueTemplate.getLinks());
	copy.setName(venueTemplate.getName());
	copy.setParent(venueTemplate.getParent());
	copy.setPostalCode(venueTemplate.getPostalCode());
	copy.setProperties(venueTemplate.getProperties());
	copy.setReference(venueTemplate.isReference());
	copy.setRegion(venueTemplate.getRegion());
	copy.setRegionAbbreviation(venueTemplate.getRegionAbbreviation());
	copy.setSource(venueTemplate.getSource());
	copy.setTags(venueTemplate.getTags());
	copy.setTimeZone(venueTemplate.getTimeZone());
	copy.setType(venueTemplate.getType());
	copy.setUrl(venueTemplate.getUrl());

	return copy;
    }

}
