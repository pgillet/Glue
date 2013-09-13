package com.glue.webapp.logic;

import java.io.IOException;

import com.glue.struct.IStream;
import com.glue.struct.IUser;
import com.glue.struct.IVenue;
import com.glue.webapp.db.DAOCommand;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.StreamDAO;
import com.glue.webapp.db.VenueDAO;
import com.glue.webapp.repository.RepositoryManager;

public class StreamController {

	public void createStream(final IStream stream, final IVenue venue, final IUser admin)
			throws InternalServerException {
		try {
			DAOManager manager = DAOManager.getInstance();

			manager.transaction(new DAOCommand<Void>() {

				@Override
				public Void execute(DAOManager manager) throws Exception {
					StreamDAO streamDAO = manager.getStreamDAO();
					VenueDAO venueDAO = manager.getVenueDAO();
					
					// Search for an existing venue
					IVenue persistentVenue = venueDAO.search(venue.getAddress());
					if(persistentVenue == null){
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
