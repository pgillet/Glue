package com.glue.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;

public class EventCAO extends VenueCAO {

    static final Logger LOG = LoggerFactory.getLogger(EventCAO.class);

    protected EventCAO() {
    }

    /**
     * Returns the folder for the given event.
     * 
     * @param event
     *            a persistent event
     * @param create
     *            <code>true</code> to create the folder for the given event if
     *            necessary; <code>false</code> to return <code>null</code> if
     *            there's no folder for the event
     * @return the folder for the given event
     */
    public Folder getFolder(Event event, boolean create) {

	String path = getPath(event).getPath();

	LOG.debug("Getting event folder by path = " + path);
	return getFolder(path, create);
    }

    /**
     * Returns the folder for the given event, or <code>null</code> if it does
     * not exist.
     * 
     * @param venue
     *            a persistent event
     * @return the folder for the given event, or <code>null</code> if it does
     *         not exist
     */
    public Folder getFolder(Event event) {
	return getFolder(event, false);
    }

    /**
     * <Venue Path>/yyyy/MM/dd/eventID
     * 
     * @param venue
     * @return
     */
    protected CmisPath getPath(Event event) {
	CmisPath venuePath = getPath(event.getVenue());

	Date startTime = event.getStartTime();
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(startTime);

	int year = calendar.get(Calendar.YEAR);
	int month = calendar.get(Calendar.MONTH) + 1;
	int day = calendar.get(Calendar.DAY_OF_MONTH);

	CmisPath eventSubPath = new CmisPath(false, Integer.toString(year),
		Integer.toString(month), Integer.toString(day), event.getId());

	return venuePath.resolve(eventSubPath);
    }

    /**
     * Creates a new document in the event folder. The input stream is consumed
     * but not closed by this method.
     * 
     * @param filename
     *            the filename, should be set
     * @param mimetype
     *            the MIME type, if unknown "application/octet-stream" should be
     *            used
     * @param input
     *            the input stream, should not be null
     * @param event
     *            the event at which the document will be added
     * @throws IOException
     *             if an I/O error occured or if the document already exists
     */
    public void add(String filename, String mimetype, InputStream input,
	    Event event) throws IOException {
	Folder folder = getFolder(event, true);
	add(filename, mimetype, input, folder);
    }

    /**
     * @see #add(String, String, InputStream, Event)
     * 
     *      <p>
     *      The document can be later retrieved by calling
     *      {@link #getDocument(String, Event)} where the String argument is the
     *      name of the given url, i.e. the text after the last forward or
     *      backslash.
     *      </p>
     */
    public void add(String url, Event event) throws IOException {
	Folder folder = getFolder(event, true);
	add(url, folder);
    }

    /**
     * Finds the document with the given name in the event's folder.
     * 
     * @param name
     *            name of the desired resource
     * @param event
     *            the parent event of the resource, from which the parent path
     *            is deduced.
     * @return A InputStream object or null if no resource with this name is
     *         found
     */
    public InputStream getDocument(String name, Event event) {
	CmisPath parent = getPath(event);
	Document doc = getDocumentObject(parent, name);
	return (doc != null ? doc.getContentStream().getStream() : null);
    }

    public InputStream getImage(String basename, Event event) {
	return getImage(basename, event, ImageRendition.ORIGINAL);
    }

    public InputStream getImage(String basename, Event event,
	    ImageRendition rendition) {
	CmisPath parent = getPath(event);
	Document doc = getImage(parent, basename, rendition);
	return (doc != null ? doc.getContentStream().getStream() : null);
    }

    /**
     * Finds the document with the given name in the event's folder.
     * 
     * @param name
     *            name of the desired resource
     * @param event
     *            the parent event of the resource, from which the parent path
     *            is deduced.
     * @return the document URI or null if no resource with this name has been
     *         found
     */
    public String getDocumentURL(String name, Event event) {
	CmisPath parent = getPath(event);
	Document doc = getDocumentObject(parent, name);
	return (doc != null ? getDocumentURL(doc) : null);
    }

    /**
     * Adds an image with the original rendition.
     * 
     * @param url
     * @param event
     * @throws IOException
     * @see {@link #addImage(String, Event, ImageRendition)}
     */
    public void addImage(String url, Event event) throws IOException {
	addImage(url, event, ImageRendition.ORIGINAL);
    }

    /**
     * Adds the image from the given url and with the specified rendition.
     * 
     * <p>
     * The image can be later retrieved by calling
     * {@link #getImage(String, Event, ImageRendition)} where the String
     * argument is the basename of the given url, i.e. the text after the last
     * forward or backslash minus the extension.
     * </p>
     * 
     * @param url
     * @param event
     * @param rendition
     * @throws IOException
     */
    public void addImage(String url, Event event, ImageRendition rendition)
	    throws IOException {
	Folder folder = getFolder(event, true);
	addImage(url, folder, rendition);
    }

    /**
     * Finds the original image with the given basename in the event's folder.
     * 
     * @param basename
     *            name of the desired image, minus the extension
     * @param event
     *            the parent event of the image, from which the parent path is
     *            deduced.
     * @return the document URI or null if no resource with this name has been
     *         found
     * @see {@link #getImageURL(String, Event, ImageRendition)}
     */
    public String getImageURL(String basename, Event event) {
	return getImageURL(basename, event, ImageRendition.ORIGINAL);
    }

    /**
     * Finds the image with the given basename and rendition in the event's
     * folder.
     * 
     * @param basename
     *            name of the desired image, minus the extension
     * @param event
     *            the parent event of the image, from which the parent path is
     *            deduced.
     * @param rendition
     *            the desired rendition
     * @return the image URL or null if no image with this basename has been
     *         found
     */
    public String getImageURL(String basename, Event event,
	    ImageRendition rendition) {
	CmisPath parent = getPath(event);
	Document doc = getImage(parent, basename, rendition);
	return (doc != null ? getDocumentURL(doc) : null);
    }

}
