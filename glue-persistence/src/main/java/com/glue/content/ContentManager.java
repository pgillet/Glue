package com.glue.content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.CapabilityRenditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentManager {

    static final Logger LOG = LoggerFactory.getLogger(ContentManager.class);

    // TODO: should be configurable
    static final String ATOMPUB_URL = "http://localhost:8080/glue-content/atom";
    static final String REPOSITORY_ID = "glue";
    private static final String USER = "glue";
    private static final String PASSWORD = "glue";

    private SessionFactory factory = SessionFactoryImpl.newInstance();

    // OpenCMIS is thread-safe
    private Session session;

    private VenueCAO venueCAO = new VenueCAO();
    private EventCAO eventCAO = new EventCAO();

    public VenueCAO getVenueCAO() {
	if (venueCAO.getSession() == null) {
	    venueCAO.setSession(getSession());
	}
	return venueCAO;
    }

    public EventCAO getEventCAO() {
	if (eventCAO.getSession() == null) {
	    eventCAO.setSession(getSession());
	}
	return eventCAO;
    }

    protected Session getSession() {
	if (session == null) {
	    session = createSession();
	}

	return session;
    }

    protected Session createSession() {
	Map<String, String> params = new HashMap<String, String>();

	// User credentials
	params.put(SessionParameter.USER, USER);
	params.put(SessionParameter.PASSWORD, PASSWORD);

	// Settings for a local connection
	// params.put(SessionParameter.BINDING_TYPE, BindingType.LOCAL.value());
	// params.put(SessionParameter.LOCAL_FACTORY,
	// "org.apache.chemistry.opencmis.fileshare.FileShareCmisServiceFactory");
	// params.put(SessionParameter.REPOSITORY_ID, REPOSITORY_ID);

	// ATOMPUB binding for connection
	params.put(SessionParameter.ATOMPUB_URL, ATOMPUB_URL);
	params.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

	// Connecting to the repository by id
	// params.put(SessionParameter.REPOSITORY_ID, REPOSITORY_ID);

	// Create session from the factory
	// Session session = factory.createSession(params);

	List<Repository> repositories = factory.getRepositories(params);
	for (Repository r : repositories) {
	    LOG.info("Found repository: " + r.getName());
	}

	// Create the session from the repository
	Repository repository = repositories.get(0);
	Session session = repository.createSession();
	LOG.info("Got a connection to repository: " + repository.getName()
		+ ", with id: " + repository.getId());

	if (session.getRepositoryInfo().getCapabilities()
		.getRenditionsCapability().equals(CapabilityRenditions.NONE)) {
	    LOG.info("Repository does not support renditions");
	}

	return session;
    }

}
