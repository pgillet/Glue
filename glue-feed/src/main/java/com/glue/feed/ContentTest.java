package com.glue.feed;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.CapabilityRenditions;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;

public class ContentTest {

	public static void main(String[] args) {
		// default factory implementation
		SessionFactory factory = SessionFactoryImpl.newInstance();
		Map<String, String> params = new HashMap<String, String>();

		// user credentials
		params.put(SessionParameter.USER, "test");
		params.put(SessionParameter.PASSWORD, "test");

		// connection settings
		// params.put(SessionParameter.BINDING_TYPE, BindingType.LOCAL.value());
		// params.put(SessionParameter.LOCAL_FACTORY,
		// "org.apache.chemistry.opencmis.fileshare.FileShareCmisServiceFactory");
		// params.put(SessionParameter.REPOSITORY_ID, "test");

		params.put(SessionParameter.ATOMPUB_URL,
				"http://localhost:8080/glue-content/atom");
		params.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
		// params.put(SessionParameter.REPOSITORY_ID, "test");

		// create session
		// Session session = factory.createSession(params);

		List<Repository> repositories = factory.getRepositories(params);
		for (Repository r : repositories) {
		    System.out.println("Found repository: " + r.getName());
		}
		
		Repository repository = repositories.get(0);
		Session session = repository.createSession();
		System.out.println("Got a connection to repository: " 
			    + repository.getName() + ", with id: "
			    + repository.getId());
		
		
		if (session.getRepositoryInfo().getCapabilities().getRenditionsCapability()
	            .equals(CapabilityRenditions.NONE)) {
	        System.out.println("Repository does not support renditions");
		}

		// *** Creating a folder
		Folder root = session.getRootFolder();

		// properties
		// (minimal set: name and object type id)
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
		properties.put(PropertyIds.NAME, "a new folder");

		// create the folder
		Folder newFolder = root.createFolder(properties);

		// *** Creating a document
		String name = "myNewDocument.txt";

		// properties
		// (minimal set: name and object type id)
		properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		properties.put(PropertyIds.NAME, name);

		// content
		byte[] content = "Hello World!".getBytes();
		InputStream stream = new ByteArrayInputStream(content);
		ContentStream contentStream = new ContentStreamImpl(name,
				BigInteger.valueOf(content.length), "text/plain", stream);

		// create a major version
		Document newDoc = newFolder.createDocument(properties, contentStream, VersioningState.NONE);

		// *** Listing the folder
		int maxItemsPerPage = 5;
		int skipCount = 0;

		CmisObject object = session.getObject(session.createObjectId(newFolder
				.getId()));
		Folder folder = (Folder) object;
		OperationContext operationContext = session.createOperationContext();
		operationContext.setMaxItemsPerPage(maxItemsPerPage);

		ItemIterable<CmisObject> children = folder
				.getChildren(operationContext);
		
//		for (CmisObject child : children) {
//		    System.out.println("---------------------------------");
//		    System.out.println("    Id:              " + child.getId());
//		    System.out.println("    Name:            " + child.getName());
//		    System.out.println("    Base Type:       " + child.getBaseTypeId());
//		    System.out.println("    Property 'bla':  " + child.getPropertyValue("bla"));
//
//		    ObjectType type = child.getType();
//		    System.out.println("    Type Id:          " + type.getId());
//		    System.out.println("    Type Name:        " + type.getDisplayName());
//		    System.out.println("    Type Query Name:  " + type.getQueryName());
//
//		    AllowableActions actions = child.getAllowableActions();
//		    System.out.println("    canGetProperties: " + actions.getAllowableActions().contains(Action.CAN_GET_PROPERTIES));
//		    System.out.println("    canDeleteObject:  " + actions.getAllowableActions().contains(Action.CAN_DELETE_OBJECT));
//		}
		
		
		
		ItemIterable<CmisObject> page = children.skipTo(skipCount).getPage();

		Iterator<CmisObject> pageItems = page.iterator();
		while (pageItems.hasNext()) {
			CmisObject item = pageItems.next();
			System.out.println("Found " + item.getName() + " in "
					+ newFolder.getName());
		}

	}

}
