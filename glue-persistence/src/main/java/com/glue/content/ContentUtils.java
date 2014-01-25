package com.glue.content;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

/**
 * A set of utility methods that simplify file and folder CMIS operations.
 * 
 * @author pgillet
 * 
 */
public class ContentUtils {

    /**
     * Tests whether the CMIS object denoted by this pathname exists.
     * 
     * @param session
     *            the CMIS session
     * @param path
     *            a pathname
     * @return true if and only if the file or directory denoted by this
     *         abstract pathname exists; false otherwise
     */
    public static boolean exists(Session session, String path) {
	try {
	    session.getObjectByPath(path);
	} catch (CmisObjectNotFoundException e1) {
	    return false;
	}

	return true;
    }

    /**
     * Creates the folder named by this pathname, including any necessary but
     * nonexistent parent folders. Note that if this operation fails it may have
     * succeeded in creating some of the necessary parent folders.
     * 
     * @param session
     *            the CMIS session
     * @param path
     *            a pathname
     * 
     * @return <code>true</code> if and only if the folder was created, along
     *         with all necessary parent folders; <code>false</code> otherwise
     */
    public static boolean createFolders(Session session, String path) {

	if (exists(session, path)) {
	    return false;
	}
	if (createFolder(session, path)) {
	    return true;
	}

	CmisPath parent = new CmisPath(path).getParent();

	return (parent != null
		&& (createFolders(session, parent.getPath()) || exists(session,
			parent.getPath())) && createFolder(session, path));
    }

    /**
     * Creates the folder named by this pathname.
     * 
     * @param session
     *            the CMIS session
     * @param path
     *            a pathname
     * 
     * @return <code>true</code> if and only if the folder was created;
     *         <code>false</code> otherwise
     */
    public static boolean createFolder(Session session, String path) {
	CmisPath cmisPath = new CmisPath(path);

	try {
	    CmisPath parent = cmisPath.getParent();
	    Folder parentFolder;
	    if (parent != null) {
		parentFolder = (Folder) session.getObjectByPath(parent
			.getPath());
	    } else {
		parentFolder = session.getRootFolder();
	    }

	    Map<String, Object> properties = new HashMap<String, Object>();
	    properties.put(PropertyIds.OBJECT_TYPE_ID,
		    BaseTypeId.CMIS_FOLDER.value());
	    properties.put(PropertyIds.NAME, cmisPath.getName());

	    parentFolder.createFolder(properties);

	    return true;

	} catch (CmisObjectNotFoundException e1) {
	    return false;
	}
    }
}
