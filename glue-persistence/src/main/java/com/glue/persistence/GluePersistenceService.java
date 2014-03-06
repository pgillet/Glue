package com.glue.persistence;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContextType;

public class GluePersistenceService extends PersistenceService {

    private static final String PERSISTENCE_UNIT = "gluedb";

    // private static final Map<String, PersistenceService> _services = new
    // HashMap<String, PersistenceService>();

    private static GluePersistenceService _service;

    protected EventDAO eventDAO = new EventDAO();
    protected VenueDAO venueDAO = new VenueDAO();
    protected PerformerDAO performerDAO = new PerformerDAO();
    protected UserDAO userDAO = new UserDAO();

    protected GluePersistenceService(String unit, EntityManagerFactory emf,
	    boolean managed, PersistenceContextType scope) {
	super(unit, emf, managed, scope);
    }

    // public synchronized static PersistenceService getService(String unit) {
    // return getService(unit, null);
    // }
    //
    // public synchronized static PersistenceService getService(String unit,
    // Map<String, Object> config) {
    // PersistenceService service = _services.get(unit);
    // if (service == null) {
    // EntityManagerFactory emf = Persistence.createEntityManagerFactory(
    // unit, config);
    // service = new GluePersistenceService(unit, emf, false,
    // PersistenceContextType.TRANSACTION);
    // _services.put(unit, service);
    // }
    // return service;
    // }

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
