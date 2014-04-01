package com.glue.content;

import java.io.InputStream;

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

    /**
     * Finds the document with the given name in the venue's folder.
     * 
     * @param name
     *            name of the desired resource
     * @param venue
     *            the parent venue of the resource, from which the parent path
     *            is deduced.
     * @return A InputStream object or null if no resource with this name is
     *         found
     */
    public InputStream getDocument(String name, Venue venue) {
	CmisPath parent = getPath(venue);
	return getDocument(parent, name);
    }

}
