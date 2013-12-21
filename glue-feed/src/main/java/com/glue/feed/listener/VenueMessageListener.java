package com.glue.feed.listener;

import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.DataSourceManager;
import com.glue.feed.FeedMessageListener;
import com.glue.struct.IVenue;
import com.glue.webapp.db.DAOCommand;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.VenueDAO;

public class VenueMessageListener implements FeedMessageListener<IVenue> {

	static final Logger LOG = LoggerFactory
			.getLogger(VenueMessageListener.class);

	private DAOManager manager;

	public VenueMessageListener() throws NamingException, SQLException {
		DataSource ds = DataSourceManager.getInstance().getDataSource();

		manager = DAOManager.getInstance(ds);
	}

	@Override
	public void newMessage(final IVenue venue) throws Exception {

		final VenueDAO venueDAO = manager.getVenueDAO();

		manager.transaction(new DAOCommand<Void>() {

			@Override
			public Void execute(DAOManager manager) throws Exception {

				// Search for an existing venue
				IVenue persistentVenue = venueDAO.searchByName(venue.getName());

				if (persistentVenue == null) {
					LOG.info("Inserting " + venue);
					persistentVenue = venueDAO.create(venue);
				} else {
					LOG.info("Venue already exists = " + venue);
				}
				return null;
			}

		});
	}

	@Override
	public void close() {
		manager.shutdownQuietly();
	}

}
