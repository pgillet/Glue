package com.glue.content;

import java.io.InputStream;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCAO {

    static final Logger LOG = LoggerFactory.getLogger(AbstractCAO.class);

    protected Session session;

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
     * @return A InputStream object or null if no resource with this name is
     *         found
     */
    protected InputStream getDocument(CmisPath parent, String name) {

	CmisPath path = parent.resolve(name);
	InputStream stream = null;
	try {
	    CmisObject object = session.getObjectByPath(path.getPath());
	    Document document = (Document) object;
	    stream = document.getContentStream().getStream();

	} catch (CmisObjectNotFoundException e) {
	    LOG.warn("Not found object with path = " + path.getPath());
	}

	return stream;
    }

}
