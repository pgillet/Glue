package com.glue.content;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

public abstract class AbstractCAO {

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

}
