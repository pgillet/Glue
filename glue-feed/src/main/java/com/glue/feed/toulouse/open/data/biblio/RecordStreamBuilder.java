package com.glue.feed.toulouse.open.data.biblio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.xml.GlueObjectBuilder;
import com.glue.struct.Category;
import com.glue.struct.IStream;
import com.glue.struct.IVenue;
import com.glue.struct.impl.Stream;
import com.glue.struct.impl.Venue;

public class RecordStreamBuilder implements GlueObjectBuilder<Record, IStream> {

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
	public IStream build(Record record) throws Exception {
		IStream stream = new Stream();

		stream.setTitle(record.title);
		stream.setCategory(getCategory(record.genre));
		stream.setDescription(record.summary);

		// Start date
		String dateSource = record.startTime;
		dateSource = dateSource.replace('h', ':');
		
		Date startDate = dateFormat.parse(dateSource);
		stream.setStartDate(startDate.getTime());

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

		stream.setEndDate(endDate.getTime());

		// Tags
		String topic = record.theme; // Only one topic
		Set<String> tags = new HashSet<>();
		tags.add(topic);

		stream.setTags(tags);

		// Thumbnail path
		String image = record.illustration;
		if (image != null && image.length() > 0) {
			stream.setThumbPath("http://www.bibliotheque.toulouse.fr/" + image);
		}

		// URL
		stream.setUrl(record.link);

		// Venue
		String location = record.where;
		if (location != null && location.length() > 0) {
			IVenue venue = new Venue();
			venue.setName(location);
			stream.setVenue(venue);
		}

		return stream;
	}

	private Category getCategory(String cat) {
		// Exposition, conférence, rencontre, atelier, lecture, club de lecture,
		// projection, conte, concert, visite, spectacle

		switch (cat) {
		case "Exposition":
			return Category.EXHIBITION;

		case "Conférence":
			return Category.CONFERENCE;

		case "Concert":
			return Category.MUSIC;

		case "Spectacle":
			return Category.PERFORMING_ART;

		default:
			return Category.OTHER;
		}
	}

}
