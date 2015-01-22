package com.glue.feed.html;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.glue.domain.Event;
import com.glue.domain.Image;
import com.glue.domain.ImageItem;
import com.glue.domain.Venue;
import com.glue.feed.time.DateTimeProcessor;

public class EventMappingStrategy implements HTMLMappingStrategy<Event> {

    private EventSelectors selectors;

    private Event eventRef = new Event();

    private DateTimeProcessor dateTimeProcessor;

    private HTMLFetcher hf = new HTMLFetcher();

    public EventMappingStrategy(EventSelectors selectors) throws IOException {
	this(selectors, null);
    }

    public EventMappingStrategy(EventSelectors selectors, Event eventRef)
	    throws IOException {
	this.selectors = selectors;
	this.eventRef = eventRef;
	dateTimeProcessor = new DateTimeProcessor();
    }

    public Event getEventRef() {
	return eventRef;
    }

    public void setEventRef(Event eventRef) {
	this.eventRef = eventRef;
    }

    @Override
    public Event parse(String url) throws Exception {

	Element doc = hf.fetch(url);

	ElementDecorator elem = new ElementDecorator(doc);

	ElementDecorator otherelem = elem.selectFirst(selectors.getRootBlock());
	elem = (otherelem != null ? otherelem : elem);

	Event event = getRefCopy();

	event.setUrl(url);
	event.setTitle(elem.selectText(selectors.getTitle(), event.getTitle()));
	event.setDescription(elem.selectHtml(selectors.getDescription(),
		event.getDescription()));

	String datePattern = selectors.getDatePattern();
	String dates = elem.selectText(selectors.getDates());
	if (datePattern != null) {

	    DateFormat df = new SimpleDateFormat(datePattern,
		    selectors.getLocale());
	    TimeZone tz = TimeZone.getTimeZone("UTC");
	    df.setTimeZone(tz);

	    Date startTime = df.parse(dates);
	    event.setStartTime(startTime);
	    event.setStopTime(startTime);
	} else {
	    boolean success = dateTimeProcessor.process(dates);

	    if (success) {
		event.setStartTime(dateTimeProcessor.getStartTime());
		event.setStopTime(dateTimeProcessor.getStopTime());
		if (event.getStopTime() == null) {
		    event.setStopTime(event.getStartTime());
		}
	    } else {
		throw new ParseException("Dates could not be found", -1);
	    }
	}

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
		image.setSource(url);
		image.setSticky(true);

		event.getImages().add(image);
	    }
	}

	event.setPrice(elem.selectText(selectors.getPrice(), event.getPrice()));

	// Things to handle for sure!
	// details.getAudience();
	// details.getEventType()

	VenueSelectors venueSelectors = selectors.getVenueSelectors();
	if (venueSelectors != null) {
	    String venueLinkQuery = selectors.getVenueLink();

	    VenueMappingStrategy vms = new VenueMappingStrategy(venueSelectors);
	    if (eventRef != null) {
		vms.setVenueRef(eventRef.getVenue());
	    }

	    Venue venue;

	    if (venueLinkQuery != null) {
		ElementDecorator other = elem.selectFirst(venueLinkQuery);
		venue = vms.parse(other.firstLink());
	    } else {
		venue = vms.parse(elem.getElement());
	    }
	    event.setVenue(venue);

	}

	return event;
    }

    /**
     * Returns a dumb copy of the reference event.
     * 
     * @return
     */
    private Event getRefCopy() {
	Event copy = new Event();

	copy.setAllDay(eventRef.isAllDay());
	copy.setCategories(eventRef.getCategories());
	copy.setCategory(eventRef.getCategory());
	copy.setChildren(eventRef.getChildren());
	copy.setComments(eventRef.getComments());
	copy.setCreated(eventRef.getCreated());
	copy.setDescription(eventRef.getDescription());
	copy.setFree(eventRef.isFree());
	copy.setGoing(eventRef.getGoing());
	// copy.setId(id);
	// copy.setImages(eventRef.getImages());
	copy.setLinks(eventRef.getLinks());
	copy.setOccurrences(eventRef.getOccurrences());
	copy.setParent(eventRef.getParent());
	copy.setPerformers(eventRef.getPerformers());
	copy.setPrice(eventRef.getPrice());
	copy.setProperties(eventRef.getProperties());
	copy.setReference(eventRef.isReference());
	copy.setSource(eventRef.getSource());
	copy.setStartTime(eventRef.getStartTime());
	copy.setStopTime(eventRef.getStopTime());
	copy.setSummary(eventRef.getSummary());
	copy.setTags(eventRef.getTags());
	copy.setTimeZone(eventRef.getTimeZone());
	copy.setTitle(eventRef.getTitle());
	copy.setUrl(eventRef.getUrl());
	copy.setVenue(eventRef.getVenue());
	copy.setWithdrawn(eventRef.isWithdrawn());
	copy.setWithdrawnNote(eventRef.getWithdrawnNote());

	return copy;
    }

}
