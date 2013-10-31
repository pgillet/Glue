package com.glue.webapp.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.naming.NamingException;

import com.glue.struct.IStream;
import com.glue.struct.IUser;
import com.glue.struct.IVenue;
import com.glue.struct.impl.Venue;
import com.glue.webapp.db.DAOCommand;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.StreamDAO;
import com.glue.webapp.db.VenueDAO;
import com.glue.webapp.repository.RepositoryManager;
import com.glue.webapp.search.SearchEngine;

public class StreamController {

	@Inject
	private SearchEngine engine;

	public List<IStream> search(String query) throws InternalServerException {

		// The underlying search engine returns only partial streams (i.e. only
		// the properties that are actually indexed)
		List<IStream> temp = engine.search(query);
		final List<Long> ids = new ArrayList<Long>();

		for (IStream stream : temp) {
			ids.add(stream.getId());
		}

		try {
			DAOManager manager = DAOManager.getInstance();
			List<IStream> streams = manager
					.transaction(new DAOCommand<List<IStream>>() {

						@Override
						public List<IStream> execute(DAOManager manager)
								throws Exception {

							StreamDAO streamDAO = manager.getStreamDAO();
							VenueDAO venueDAO = manager.getVenueDAO();

							List<IStream> items = streamDAO.searchInList(ids
									.toArray(new Long[ids.size()]));

							for (IStream stream : items) {

								// TODO
								stream.setVenue(new Venue());
							}

							return items;
						}

					});

			return streams;
		} catch (NamingException e) {
			throw new InternalServerException(e);
		} catch (Exception e) {
			throw new InternalServerException(e);
		}
	}

	public void createStream(final IStream stream, final IVenue venue,
			final IUser admin) throws InternalServerException {
		try {
			DAOManager manager = DAOManager.getInstance();

			manager.transaction(new DAOCommand<Void>() {

				@Override
				public Void execute(DAOManager manager) throws Exception {
					StreamDAO streamDAO = manager.getStreamDAO();
					VenueDAO venueDAO = manager.getVenueDAO();

					// Search for an existing venue
					IVenue persistentVenue = venueDAO.search(venue.getAddress());
					if (persistentVenue == null) {
						persistentVenue = venueDAO.create(venue);
					}

					stream.setVenue(persistentVenue);
					streamDAO.create(stream);

					// Set user as administrator
					streamDAO.joinAsAdmin(stream.getId(), admin.getId());

					// Create associated directory
					if (!RepositoryManager.createStream(stream.getId())) {
						throw new IOException("Cannot create stream directory");
					}

					return null;
				}
			});

		} catch (Exception e) {
			throw new InternalServerException(e);
		}
	}

}
