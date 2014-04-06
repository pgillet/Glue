package com.glue.content;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.bindings.spi.atompub.AbstractAtomPubService;
import org.apache.chemistry.opencmis.client.bindings.spi.atompub.AtomPubParser;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	    LOG.warn("Not found object with path = " + path.getPath());
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

}
