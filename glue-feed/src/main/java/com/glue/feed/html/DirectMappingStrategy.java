package com.glue.feed.html;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.glue.domain.IStream;
import com.glue.domain.IVenue;
import com.glue.domain.impl.Stream;
import com.glue.domain.impl.Venue;

public class DirectMappingStrategy implements HTMLMappingStrategy<IStream> {

    private EventDetailsPage details;

    public DirectMappingStrategy(EventDetailsPage details) {
	this.details = details;
    }

    @Override
    public IStream parse(String url) throws Exception {

	Element doc = Jsoup.connect(url).get();

	if (details.getRootBlock() != null) {
	    Elements elems = doc.select(details.getRootBlock());
	    if (!elems.isEmpty()) {
		doc = elems.first();
	    }
	}

	IStream stream = new Stream();

	stream.setUrl(url);
	stream.setTitle(HTMLUtils.selectText(details.getTitle(), doc));
	stream.setDescription(HTMLUtils.selectHtml(details.getDescription(),
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
	    stream.setStartDate(startDate.getTime());

	    String endDateQuery = details.getEndDate();
	    if (endDateQuery != null) {
		String endSource = HTMLUtils.selectText(endDateQuery, doc);
		if (endSource != null) {
		    Date endDate = df.parse(endSource);
		    stream.setEndDate(endDate.getTime());
		}
	    }
	}

	String thumbnailQuery = details.getThumbnail();
	if (thumbnailQuery != null) {
	    Elements elems = doc.select(thumbnailQuery);
	    // Get media
	    elems = elems.select("[src]");

	    if (!elems.isEmpty()) {
		stream.setThumbPath(elems.attr("abs:src"));
	    }
	}

	String priceQuery = details.getPrice();
	if (priceQuery != null) {
	    stream.setPrice(HTMLUtils.selectText(priceQuery, doc));
	}

	String venueNameQuery = details.getVenueName();
	if (venueNameQuery != null) {
	    IVenue venue = new Venue();
	    venue.setName(HTMLUtils.selectText(venueNameQuery, doc));

	    String venueAddressQuery = details.getVenueAddress();
	    if (venueAddressQuery != null) {
		venue.setAddress(HTMLUtils.selectText(venueAddressQuery, doc));
	    }

	    stream.setVenue(venue);
	}

	// Thing to handle for sure!
	// stream.setCategory(category);
	// details.getAudience();
	// details.getEventType()

	return stream;
    }

}
