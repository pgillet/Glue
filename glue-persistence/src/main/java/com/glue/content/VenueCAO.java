package com.glue.content;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Venue;

public class VenueCAO extends AbstractCAO {

    static final Logger LOG = LoggerFactory.getLogger(VenueCAO.class);

    protected VenueCAO() {
    }

    /**
     * Returns the folder for the given venue.
     * 
     * @param venue
     *            a persistent venue
     * @param create
     *            <code>true</code> to create the folder for the given venue if
     *            necessary; <code>false</code> to return <code>null</code> if
     *            there's no folder for the venue
     * @return the folder for the given venue
     */
    public Folder getFolder(Venue venue, boolean create) {

	String path = getPath(venue).getPath();

	LOG.debug("Getting venue folder by path = " + path);
	return getFolder(path, create);
    }

    /**
     * Returns the folder for the given venue, or <code>null</code> if it does
     * not exist.
     * 
     * @param venue
     *            a persistent venue
     * @return the folder for the given venue, or <code>null</code> if it does
     *         not exist
     */
    public Folder getFolder(Venue venue) {
	return getFolder(venue, false);
    }

    /**
     * City/venueID/
     * 
     * @param venue
     * @return
     */
    protected CmisPath getPath(Venue venue) {
	return new CmisPath(true, venue.getCity(), venue.getId());
    }

}
