package com.glue.feed.rss;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.glue.struct.IStream;
import com.glue.struct.IVenue;
import com.glue.struct.impl.Stream;
import com.glue.struct.impl.Venue;

public class BikiniMessageListener implements FeedMessageListener {

	List<IStream> streams = new ArrayList<IStream>();
	Set<IVenue> venues = new HashSet<IVenue>();

	@Override
	public void newMessage(FeedMessage msg) throws Exception {

		// Title
		int index = msg.getTitle().lastIndexOf(":");
		String title = msg.getTitle().substring(0, index).trim();
		// System.out.println("title = " + title);

		// Begin date
		String strdate = msg.getTitle().substring(index + 1).trim();
		DateFormat format = new SimpleDateFormat("'le' E dd MMM yyyy",
				Locale.FRENCH); // ex: "le vendredi 20 septembre 2013"
		Date date = format.parse(strdate);
		// System.out.println("Date = " + date);

		// Link
		String url = msg.getLink();
		// System.out.println("Link = " + url);

		// Description
		index = msg.getDescription().lastIndexOf("<br>");
		String description = msg.getDescription().substring(0, index);
		// System.out.println("Description = " + description);

		// Venue address
		index = msg.getDescription().lastIndexOf(":");
		String address = msg.getDescription().substring(index + 1).trim();
		// System.out.println("Venue address = " + address);

		// Venue name
		index = address.lastIndexOf("(");
		String name = address.substring(0, index).trim();
		// System.out.println("Venue name = " + name);

		IStream stream = new Stream();
		stream.setTitle(title);
		stream.setDescription(description);
		stream.setUrl(url);
		stream.setPublicc(true);
		stream.setOpen(true);
		stream.setStartDate(date.getTime());
		streams.add(stream);

		IVenue venue = new Venue();
		venue.setName(name);
		venue.setAddress(address);

		venues.add(venue);
	}

	/**
	 * @return the streams
	 */
	public List<IStream> getStreams() {
		return streams;
	}

	/**
	 * @return the venues
	 */
	public Set<IVenue> getVenues() {
		return venues;
	}

}
