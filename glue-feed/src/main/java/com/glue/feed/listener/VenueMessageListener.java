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

	static final Logger LOG = LoggerFactory.getLogger(VenueMessageListener.class);

	private DAOManager manager;
	private VenueDAO venueDAO;

	public VenueMessageListener() throws NamingException, SQLException {
		DataSource ds = DataSourceManager.getInstance().getDataSource();

		manager = DAOManager.getInstance(ds);
		venueDAO = manager.getVenueDAO();
	}

	@Override
	public void newMessage(final IVenue venue) {

		try {
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

			}, false);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

	}

	@Override
	public void close() {
		manager.closeConnectionQuietly();
	}

}
