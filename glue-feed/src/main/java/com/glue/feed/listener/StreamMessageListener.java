package com.glue.feed.listener;

import java.sql.SQLException;
import java.util.Set;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.IStream;
import com.glue.domain.IVenue;
import com.glue.feed.DataSourceManager;
import com.glue.feed.FeedMessageListener;
import com.glue.webapp.db.DAOCommand;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.StreamDAO;
import com.glue.webapp.db.TagDAO;
import com.glue.webapp.db.VenueDAO;

public class StreamMessageListener implements FeedMessageListener<IStream> {

	static final Logger LOG = LoggerFactory.getLogger(StreamMessageListener.class);

	private DAOManager manager;

	public StreamMessageListener() throws NamingException, SQLException {
		DataSource ds = DataSourceManager.getInstance().getDataSource();
		manager = DAOManager.getInstance(ds);
	}

	@Override
	public void newMessage(final IStream stream) throws Exception {

		final StreamDAO streamDAO = manager.getStreamDAO(true); // with indexing
		final VenueDAO venueDAO = manager.getVenueDAO();
		final TagDAO tagDAO = manager.getTagDAO();

		manager.transaction(new DAOCommand<Void>() {

			@Override
			public Void execute(DAOManager manager) throws Exception {

				IVenue venue = stream.getVenue();
				if (venue == null) {
					LOG.trace("Streams without a venue are not allowed");
					return null;
				}

				// Search for an existing venue
				IVenue persistentVenue = venueDAO.searchForDuplicate(venue);
				if (persistentVenue == null) {
					LOG.info("Inserting " + venue);
					persistentVenue = venueDAO.create(venue);
				} else {
					LOG.info("Venue already exists = " + venue);
				}
				stream.setVenue(persistentVenue);

				if (!streamDAO.exists(stream)) {
					LOG.info("Inserting " + stream);
					streamDAO.create(stream);

					// Stream tags
					Set<String> tags = stream.getTags();
					if (tags != null && !tags.isEmpty()) {
						for (String tag : tags) {
							LOG.info("Adding tag = " + tag);
							tagDAO.addTag(tag, stream.getId());
						}
					}
				} else {
					LOG.info("Stream already exists = " + stream);
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
