package com.glue.feed.toulouse.open.data.biblio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.EventCategory;
import com.glue.domain.Image;
import com.glue.domain.ImageItem;
import com.glue.domain.Tag;
import com.glue.domain.Venue;
import com.glue.feed.GlueObjectBuilder;

public class RecordStreamBuilder implements GlueObjectBuilder<Record, Event> {

    static final Logger LOG = LoggerFactory
	    .getLogger(RecordStreamBuilder.class);

    private static final String date_pattern = "dd/MM/yyyy HH:mm";
    private static final String time_pattern = "HH:mm";

    private DateFormat dateFormat;
    private DateFormat timeFormat;

    public RecordStreamBuilder() {
	TimeZone tz = TimeZone.getTimeZone("UTC");
	dateFormat = new SimpleDateFormat(date_pattern);
	dateFormat.setTimeZone(tz);
	timeFormat = new SimpleDateFormat(time_pattern);
	timeFormat.setTimeZone(tz);
    }

    @Override
    public Event build(Record record) throws Exception {
	Event event = new Event();

	event.setTitle(record.title);
	event.setCategory(getCategory(record.genre));
	event.setDescription(record.summary);

	// Start date
	String dateSource = record.startTime;
	dateSource = dateSource.replace('h', ':');

	Date startDate = dateFormat.parse(dateSource);
	event.setStartTime(startDate);

	// End date
	Date endDate = null;
	try {
	    // First, try dd/MM/yyyy HH:mm
	    dateSource = record.endTime;
	    dateSource = dateSource.replace('h', ':');
	    endDate = dateFormat.parse(dateSource);
	} catch (ParseException e) {
	    // Then, try HH'h'mm
	    endDate = timeFormat.parse(record.endTime.trim());
	    Calendar endCal = Calendar.getInstance();
	    endCal.setTime(endDate);

	    Calendar cal = Calendar.getInstance();
	    cal.setTime(startDate);
	    cal.set(Calendar.HOUR_OF_DAY, endCal.get(Calendar.HOUR_OF_DAY));
	    cal.set(Calendar.MINUTE, endCal.get(Calendar.MINUTE));

	    endDate = cal.getTime();
	}

	if (endDate.before(startDate)) {
	    // Ex: 14/01/2014 18h00 and 14/01/2014 00:00 as the end time is not
	    // significant
	    endDate = startDate;
	}
	event.setStopTime(endDate);

	// Tags
	String topic = record.theme; // Only one topic
	Tag tag = new Tag();
	tag.setTitle(topic);
	event.getTags().add(tag);

	// Thumbnail path
	String src = record.illustration;
	if (src != null && src.length() > 0 && !"NULL".equals(src)) {

	    String url = "http://www.bibliotheque.toulouse.fr/" + src;

	    ImageItem item = new ImageItem();
	    item.setUrl(url);

	    Image image = new Image();
	    image.setUrl(url);
	    image.setOriginal(item);
	    image.setSource("OPEN DATA");
	    image.setSticky(true);

	    event.getImages().add(image);
	}

	// URL
	event.setUrl(record.link);

	// Venue
	String location = record.where;
	if (location != null && location.length() > 0) {
	    Venue venue = new Venue();
	    venue.setName(location);
	    venue.setCity("Toulouse");
	    event.setVenue(venue);
	}

	return event;
    }

    private EventCategory getCategory(String cat) {
	// Exposition, conférence, rencontre, atelier, lecture, club de lecture,
	// projection, conte, concert, visite, spectacle

	switch (cat) {
	case "Exposition":
	    return EventCategory.EXHIBITION;

	case "Conférence":
	    return EventCategory.CONFERENCE;

	case "Concert":
	    return EventCategory.MUSIC;

	case "Spectacle":
	    return EventCategory.PERFORMING_ART;

	default:
	    return EventCategory.OTHER;
	}
    }

}
