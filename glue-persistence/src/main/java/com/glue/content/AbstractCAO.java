package com.glue.content;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.bindings.spi.atompub.AbstractAtomPubService;
import org.apache.chemistry.opencmis.client.bindings.spi.atompub.AtomPubParser;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;

public abstract class AbstractCAO {

    static final Logger LOG = LoggerFactory.getLogger(AbstractCAO.class);

    protected Session session;

    private Method loadLink;

    public Session getSession() {
	return session;
    }

    public void setSession(Session session) {
	this.session = session;
    }

    /**
     * Returns the folder for the given path.
     * 
     * @param path
     *            the given path
     * @param create
     *            <code>true</code> to create the folder for the given path if
     *            necessary; <code>false</code> to return <code>null</code> if
     *            there's no folder for this path
     * @return the folder for the given path
     */
    protected Folder getFolder(String path, boolean create) {
	if (create) {
	    ContentUtils.createFolders(session, path);
	}

	try {
	    Folder folder = (Folder) session.getObjectByPath(path);
	    return folder;
	} catch (CmisObjectNotFoundException e) {
	    return null;
	}
    }

    /**
     * Finds the document with the given name in the parent folder.
     * 
     * @param name
     *            name of the desired resource
     * @param parent
     *            the path of the parent folder
     * @return A Document object or null if no resource with this name is found
     */
    protected Document getDocumentObject(CmisPath parent, String name) {
	CmisPath path = parent.resolve(name);
	Document document = null;

	try {
	    CmisObject object = session.getObjectByPath(path.getPath());
	    document = (Document) object;

	} catch (CmisObjectNotFoundException e) {
	    LOG.warn("No object found with path = " + path.getPath());
	}

	return document;
    }

    private Method getLoadLinkMethod() throws NoSuchMethodException,
	    SecurityException {
	if (loadLink == null) {
	    loadLink = AbstractAtomPubService.class.getDeclaredMethod(
		    "loadLink",

		    new Class[] { String.class, String.class, String.class,
			    String.class });

	    loadLink.setAccessible(true);
	}

	return loadLink;
    }

    /**
     * Returns the document URI.
     * 
     * URL pattern that OpenCMIS uses in the AtomPub binding
     * http://<HOST>:<PORT>
     * /<SERVLET-PATH>/<REPOSITORY-ID>/<RESOURCE>?<PARAM=VALUE
     * >&<PARAM=VALUE>&...
     * 
     * @see https://chemistry.apache.org/java/developing/dev-url.html
     */
    protected String getDocumentURL(final ObjectId document) {

	String link = null;

	try {
	    Method loadLink = getLoadLinkMethod();

	    link = (String) loadLink.invoke(session.getBinding()
		    .getObjectService(), session.getRepositoryInfo().getId(),
		    document.getId(), AtomPubParser.LINK_REL_CONTENT, null);

	    if (StringUtils.isNotBlank(SessionParams.getExternalInetAddress())) {
		URIBuilder ub = new URIBuilder(link);
		ub.setHost(SessionParams.getExternalInetAddress());
		link = ub.toString();
	    }

	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}

	return link;
    }

    /**
     * Returns the document URI.
     * 
     * Alternative to {@link #getDocumentURL(ObjectId)}.
     * 
     * URL pattern that OpenCMIS uses in the AtomPub binding
     * http://<HOST>:<PORT>
     * /<SERVLET-PATH>/<REPOSITORY-ID>/<RESOURCE>?<PARAM=VALUE
     * >&<PARAM=VALUE>&...
     * 
     * @see https://chemistry.apache.org/java/developing/dev-url.html
     */
    protected String getDocumentURL0(ObjectId doc) {
	try {
	    URI uri = new URI(SessionParams.getAtompubUrl());
	    Path path = Paths.get(uri.getPath(), session.getRepositoryInfo()
		    .getId(), "content");
	    URIBuilder ub = new URIBuilder(uri).setPath(path.toString())
		    .addParameter("id", doc.getId());

	    if (StringUtils.isNotBlank(SessionParams.getExternalInetAddress())) {
		ub.setHost(SessionParams.getExternalInetAddress());
	    }

	    return ub.build().toString();
	} catch (URISyntaxException e) {
	    // should not be here as the URI is fully built with internal
	    // elements
	    LOG.error(e.getMessage(), e);
	}

	return null;
    }

    /**
     * Creates a new document in the given folder. The input stream is consumed
     * but not closed by this method.
     * 
     * @param filename
     *            the filename, should be set
     * @param mimetype
     *            the MIME type, if unknown "application/octet-stream" should be
     *            used
     * @param input
     *            the input stream, should not be null
     * @param folder
     *            the event in which the document will be added
     * @throws IOException
     *             if an I/O error occured or if the document already exists
     */
    protected void add(String filename, String mimetype, InputStream input,
	    Folder folder) throws IOException {
	ContentStream contentStream = session.getObjectFactory()
		.createContentStream(filename, -1, mimetype, input);

	Map<String, Object> properties = new HashMap<String, Object>();
	properties.put(PropertyIds.OBJECT_TYPE_ID,
		BaseTypeId.CMIS_DOCUMENT.value());
	properties.put(PropertyIds.NAME, filename);

	try {
	    Document doc = folder.createDocument(properties, contentStream,
		    VersioningState.NONE);

	    LOG.info("Created Document ID: " + doc.getId());
	} catch (CmisNameConstraintViolationException e) {
	    LOG.error(e.getMessage(), e);
	    throw new IOException(e.getMessage(), e);
	}
    }

    /**
     * @see #add(String, String, InputStream, Folder)
     * 
     *      <p>
     *      The document can be later retrieved by calling
     *      {@link #getDocument(String, Event)} where the String argument is the
     *      name of the given url, i.e. the text after the last forward or
     *      backslash.
     *      </p>
     */
    protected void add(String url, Folder folder) throws IOException {
	String filename = FilenameUtils.getName(url);
	URL urlObj = new URL(url);
	URLConnection conn = urlObj.openConnection();
	String contentType = conn.getContentType();
	InputStream input = conn.getInputStream();

	add(filename, contentType, input, folder);
    }

    /**
     * Adds an image from the given URL into the folder with the default
     * original rendition
     * 
     * @param url
     * @param folder
     * @throws IOException
     */
    protected void addImage(String url, Folder folder) throws IOException {
	addImage(url, folder, ImageRendition.ORIGINAL);
    }

    protected void addImage(String url, Folder folder, ImageRendition rendition)
	    throws IOException {
	String basename = FilenameUtils.getBaseName(url);
	String extension = FilenameUtils.getExtension(url);

	String filename = basename + "." + rendition.name().toLowerCase() + "."
		+ extension;

	URL urlObj = new URL(url);
	URLConnection conn = urlObj.openConnection();
	String contentType = conn.getContentType();

	InputStream input = conn.getInputStream();

	add(filename, contentType, input, folder);
    }

    protected Document getImage(CmisPath parent, String basename) {
	return getImage(parent, basename, ImageRendition.ORIGINAL);
    }

    /**
     * Returns the image in the parent folder with the specified basename and
     * rendition. if there is no image with the wanted rendition, returns the
     * image with the upper level rendition, or null if there is no such image.
     * 
     * @param parent
     * @param basename
     * @param rendition
     * @return
     */
    protected Document getImage(CmisPath parent, String basename,
	    ImageRendition rendition) {

	try {
	    CmisObject object = session.getObjectByPath(parent.getPath());
	    Folder folder = (Folder) object;

	    ItemIterable<CmisObject> children = folder.getChildren();

	    for (CmisObject o : children) {
		ImageRendition r = rendition;
		if (BaseTypeId.CMIS_DOCUMENT.equals(o.getBaseTypeId())) {
		    do {
			String prefix = basename + "." + r.name().toLowerCase();
			if (o.getName().startsWith(prefix)) {
			    return (Document) o;
			}
		    } while ((r = r.upper()) != null);
		}
	    }
	} catch (CmisObjectNotFoundException e) {
	    LOG.warn("No object found with path = " + parent.getPath());
	}

	return null;
    }

}
