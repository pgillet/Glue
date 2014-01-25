package com.glue.content;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.IStream;

public class StreamCAO extends VenueCAO {

    static final Logger LOG = LoggerFactory.getLogger(StreamCAO.class);

    protected StreamCAO() {
    }

    /**
     * Returns the folder for the given stream.
     * 
     * @param stream
     *            a persistent stream
     * @param create
     *            <code>true</code> to create the folder for the given stream if
     *            necessary; <code>false</code> to return <code>null</code> if
     *            there's no folder for the stream
     * @return the folder for the given stream
     */
    public Folder getFolder(IStream stream, boolean create) {

	String path = getPath(stream).getPath();

	LOG.debug("Getting stream folder by path = " + path);
	return getFolder(path, create);
    }

    /**
     * Returns the folder for the given stream, or <code>null</code> if it does
     * not exist.
     * 
     * @param venue
     *            a persistent stream
     * @return the folder for the given stream, or <code>null</code> if it does
     *         not exist
     */
    public Folder getFolder(IStream stream) {
	return getFolder(stream, false);
    }

    /**
     * <Venue Path>/yyyy/MM/dd/streamID
     * 
     * @param venue
     * @return
     */
    protected CmisPath getPath(IStream stream) {
	CmisPath venuePath = getPath(stream.getVenue());

	long startDate = stream.getStartDate();
	Calendar calendar = Calendar.getInstance();
	calendar.setTimeInMillis(startDate);

	int year = calendar.get(Calendar.YEAR);
	int month = calendar.get(Calendar.MONTH) + 1;
	int day = calendar.get(Calendar.DAY_OF_MONTH);

	CmisPath streamSubPath = new CmisPath(false, Integer.toString(year),
		Integer.toString(month), Integer.toString(day),
		Long.toString(stream.getId()));

	return venuePath.resolve(streamSubPath);
    }

    /**
     * Creates a new document in the stream folder. The input stream is consumed
     * but not closed by this method.
     * 
     * @param filename
     *            the filename, should be set
     * @param mimetype
     *            the MIME type, if unknown "application/octet-stream" should be
     *            used
     * @param input
     *            the input stream, should not be null
     * @param stream
     *            the stream at which the document will be added
     * @throws IOException
     *             if an I/O error occured or if the document already exists
     */
    public void add(String filename, String mimetype, InputStream input,
	    IStream stream) throws IOException {

	ContentStream contentStream = session.getObjectFactory()
		.createContentStream(filename, -1, mimetype, input);

	Map<String, Object> properties = new HashMap<String, Object>();
	properties.put(PropertyIds.OBJECT_TYPE_ID,
		BaseTypeId.CMIS_DOCUMENT.value());
	properties.put(PropertyIds.NAME, filename);

	Folder streamFolder = getFolder(stream, true);

	try {
	    Document doc = streamFolder.createDocument(properties,
		    contentStream, VersioningState.NONE);

	    LOG.info("Created Document ID: " + doc.getId());
	} catch (CmisNameConstraintViolationException e) {
	    LOG.error(e.getMessage(), e);
	    throw new IOException(e.getMessage(), e);
	}
    }

    /**
     * @see #add(String, String, InputStream, IStream)
     */
    public void add(URL url, IStream stream) throws IOException {

	String filename = FilenameUtils.getName(url.getPath());

	URLConnection conn = url.openConnection();
	String contentType = conn.getContentType();

	InputStream input = conn.getInputStream();

	add(filename, contentType, input, stream);

    }

}
