package com.glue.persistence;

import java.io.IOException;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContextType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A persistence service with an application-managed entity manager.
 * 
 * @author pgillet
 * 
 */
public class GluePersistenceService extends PersistenceService {

    static final Logger LOG = LoggerFactory
	    .getLogger(GluePersistenceService.class);

    public static final String PERSISTENCE_UNIT = "gluedb";

    private static GluePersistenceService _service;

    private EventDAO eventDAO = new EventDAO();
    private VenueDAO venueDAO = new VenueDAO();
    private PerformerDAO performerDAO = new PerformerDAO();
    private UserDAO userDAO = new UserDAO();

    protected GluePersistenceService(String unit, EntityManagerFactory emf,
	    boolean managed, PersistenceContextType scope) {
	super(unit, emf, managed, scope);
    }

    protected GluePersistenceService() {
	this(PERSISTENCE_UNIT, Persistence
		.createEntityManagerFactory(PERSISTENCE_UNIT), false,
		PersistenceContextType.TRANSACTION);
    }

    public synchronized static GluePersistenceService getService() {
	return getService(null);
    }

    public synchronized static GluePersistenceService getService(
	    Map<String, Object> config) {
	if (_service == null) {
	    EntityManagerFactory emf = Persistence.createEntityManagerFactory(
		    PERSISTENCE_UNIT, config);
	    _service = new GluePersistenceService(PERSISTENCE_UNIT, emf, false,
		    PersistenceContextType.TRANSACTION);
	}
	return _service;
    }

    /**
     * @return the venueDAO
     */
    protected VenueDAO getVenueDAO() {
	venueDAO.setEntityManager(getEntityManager());
	return venueDAO;
    }

    /**
     * @return the performerDAO
     */
    protected PerformerDAO getPerformerDAO() {
	performerDAO.setEntityManager(getEntityManager());
	return performerDAO;
    }

    /**
     * @return the userDAO
     */
    protected UserDAO getUserDAO() {
	userDAO.setEntityManager(getEntityManager());
	return userDAO;
    }

    /**
     * @return the eventDAO
     */
    protected EventDAO getEventDAO() {
	eventDAO.setEntityManager(getEntityManager());
	return eventDAO;
    }

    @Override
    public void close() {
	super.close();
	try {
	    EntityListenerRealm.flush();
	} catch (IOException e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * Tests the connection to the database, and returns true if a session could
     * be opened, false otherwise.
     * 
     */
    public boolean ping() {
	try {
	    EntityManager em = getEntityManager();
	    em.createNativeQuery("select 1 from DUAL").getSingleResult();
	    return true;
	} catch (Exception e) {
	    return false;
	}
    }

}
