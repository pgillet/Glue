package com.glue.feed.rss;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.naming.NamingException;
import javax.sql.DataSource;

import com.glue.feed.DataSourceManager;
import com.glue.struct.IStream;
import com.glue.struct.IVenue;
import com.glue.struct.impl.Stream;
import com.glue.struct.impl.Venue;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.StreamDAO;
import com.glue.webapp.db.VenueDAO;

public class BikiniMessageListener implements FeedMessageListener {

	private DAOManager manager;
	private StreamDAO streamDAO;
	private VenueDAO venueDAO;

	public BikiniMessageListener() throws NamingException, SQLException {
		DataSource ds = DataSourceManager.getInstance().getDataSource();

		manager = DAOManager.getInstance(ds);
		streamDAO = manager.getStreamDAO();
		venueDAO = manager.getVenueDAO();
	}

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

		IVenue venue = new Venue();
		venue.setName(name);
		venue.setAddress(address);

		// Search for an existing venue
		IVenue persistentVenue = venueDAO.search(venue.getAddress());
		if (persistentVenue == null) {
			System.out.println("Inserting " + venue);
			persistentVenue = venueDAO.create(venue);
		}

		stream.setVenue(persistentVenue);

		System.out.println("Inserting " + stream);
		streamDAO.create(stream);
	}

	@Override
	public void close() {
		manager.closeConnectionQuietly();
	}

}
