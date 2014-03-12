package com.glue.feed.html;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.glue.domain.Event;
import com.glue.domain.Image;
import com.glue.domain.ImageItem;
import com.glue.domain.Venue;

public class DirectMappingStrategy implements HTMLMappingStrategy<Event> {

    private EventDetailsPage details;

    public DirectMappingStrategy(EventDetailsPage details) {
	this.details = details;
    }

    @Override
    public Event parse(String url) throws Exception {

	Element doc = Jsoup.connect(url).get();

	if (details.getRootBlock() != null) {
	    Elements elems = doc.select(details.getRootBlock());
	    if (!elems.isEmpty()) {
		doc = elems.first();
	    }
	}

	Event event = new Event();

	event.setUrl(url);
	event.setTitle(HTMLUtils.selectText(details.getTitle(), doc));
	event.setDescription(HTMLUtils.selectHtml(details.getDescription(),
		doc));

	String datePattern = details.getDatePattern();
	if (datePattern != null) {

	    DateFormat df = new SimpleDateFormat(datePattern,
		    details.getLocale());
	    TimeZone tz = TimeZone.getTimeZone("UTC");
	    df.setTimeZone(tz);

	    String startSource = HTMLUtils.selectText(details.getStartDate(),
		    doc);
	    Date startDate = df.parse(startSource);
	    event.setStartTime(startDate);

	    String endDateQuery = details.getEndDate();
	    if (endDateQuery != null) {
		String endSource = HTMLUtils.selectText(endDateQuery, doc);
		if (endSource != null) {
		    Date endDate = df.parse(endSource);
		    event.setStopTime(endDate);
		}
	    }
	}

	String thumbnailQuery = details.getThumbnail();
	if (thumbnailQuery != null) {
	    Elements elems = doc.select(thumbnailQuery);
	    // Get media
	    elems = elems.select("[src]");

	    if (!elems.isEmpty()) {

		ImageItem item = new ImageItem();
		item.setUrl(elems.attr("abs:src"));

		Image image = new Image();
		image.setOriginal(item);
		image.setSource(url);
		image.setSticky(true);

		event.getImages().add(image);
	    }
	}

	String priceQuery = details.getPrice();
	if (priceQuery != null) {
	    event.setPrice(HTMLUtils.selectText(priceQuery, doc));
	}

	String venueNameQuery = details.getVenueName();
	if (venueNameQuery != null) {
	    Venue venue = new Venue();
	    venue.setName(HTMLUtils.selectText(venueNameQuery, doc));

	    String venueAddressQuery = details.getVenueAddress();
	    if (venueAddressQuery != null) {
		venue.setAddress(HTMLUtils.selectText(venueAddressQuery, doc));
	    }

	    event.setVenue(venue);
	}

	// Thing to handle for sure!
	// stream.setCategory(category);
	// details.getAudience();
	// details.getEventType()

	return event;
    }

}
