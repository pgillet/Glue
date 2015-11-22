package com.glue.feed.html;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.glue.domain.Event;
import com.glue.domain.Image;
import com.glue.domain.ImageItem;
import com.glue.domain.Occurrence;
import com.glue.domain.Venue;
import com.glue.feed.time.DateTimeProcessor;
import com.glue.feed.time.DateTimeProcessor.Interval;

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
    public Event parse(Element e) throws Exception {

	Element elem = e;
	Elements elems;
	String location = null;

	if ("a".equals(elem.tagName())) {
	    location = elem.attr("abs:href");
	    elem = hf.fetch(location);
	}

	if (selectors.getRootBlock() != null) {
	    Elements tmp = elem.select(selectors.getRootBlock());
	    Validate.single(tmp);
	    elem = tmp.get(0);
	}

	Event event = getRefCopy();

	event.setUrl(location);

	// Title
	event.setTitle(StringUtils.defaultIfEmpty(
		elem.select(selectors.getTitle()).text(), event.getTitle()));

	// Description
	if (selectors.getDescription() != null) {
	    elems = elem.select(selectors.getDescription());
	    Validate.notEmpty(elems);

	    String description = elems.html();
	    description = HtmlUtils.cleanHtml(description);

	    event.setDescription(StringUtils.defaultIfBlank(description,
		    event.getDescription()));
	}

	// Dates
	String datePattern = selectors.getDatePattern();
	elems = elem.select(selectors.getDates());
	Validate.single(elems);

	String dates = elems.text(); // html() ?

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

		Set<Interval> intervals = dateTimeProcessor.getIntervals();
		if (!intervals.isEmpty()) {
		    Interval it = dateTimeProcessor.getLastElement(intervals);

		    event.setStartTime(it.getStartTime());
		    event.setStopTime(it.getStopTime());
		} else {

		    Set<Date> datesCol = dateTimeProcessor.getDates();
		    if (datesCol.size() == 1) {
			Date d = datesCol.iterator().next();
			event.setStartTime(d);
			event.setStopTime(d);
		    } else {
			for (Date date : datesCol) {
			    Occurrence occur = new Occurrence();
			    occur.setStartTime(date);
			    occur.setStopTime(date);
			}
		    }
		}

	    } else {
		throw new ParseException("Dates could not be found", -1);
	    }
	}

	// Thumbnail
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

		event.getImages().add(image);
	    }
	}

	// Price
	if(selectors.getPrice() != null) {
	    String price = elem.select(selectors.getPrice()).text();
	    price = StringUtils.defaultIfBlank(price, event.getPrice());
	    event.setPrice(price);
	}

	// Things to handle for sure!
	// details.getAudience();
	// details.getEventType()

	// Venue
	VenueSelectors venueSelectors = selectors.getVenueSelectors();
	if (venueSelectors != null) {

	    // Venue description for each event description

	    VenueMappingStrategy vms = new VenueMappingStrategy(venueSelectors);
	    if (eventRef != null) {
		vms.setVenueRef(eventRef.getVenue());
	    }

	    Venue venue = vms.parse(elem);
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
