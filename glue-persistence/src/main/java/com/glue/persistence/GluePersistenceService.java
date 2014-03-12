package com.glue.persistence;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContextType;

/**
 * A persistence service with an application-managed entity manager.
 * 
 * @author pgillet
 * 
 */
public class GluePersistenceService extends PersistenceService {

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

}
