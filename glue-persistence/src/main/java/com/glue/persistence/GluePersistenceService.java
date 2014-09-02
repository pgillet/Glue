package com.glue.persistence;

import java.io.IOException;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContextType;

import org.apache.openjpa.event.TransactionListener;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerSPI;
import org.apache.openjpa.persistence.OpenJPAPersistence;
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
    private TagDAO tagDAO = new TagDAO();

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

    @Override
    protected EntityManager getEntityManager() {

	EntityManager em = super.getEntityManager();

	// Get the OpenJPA facade to the given entity manager
	OpenJPAEntityManager facade = OpenJPAPersistence.cast(em);
	if (facade != null) {
	    OpenJPAEntityManagerSPI facadeSPI = (OpenJPAEntityManagerSPI) facade;

	    for (TransactionListener listener : TransactionListenerRealm
		    .getListeners()) {
		facadeSPI.addTransactionListener(listener);
	    }
	}

	return em;
    }

    /**
     * Sets whether the entity manager will cache database queries during its
     * lifetime.
     * 
     * @param flag
     */
    protected void setQuerySQLCache(boolean flag) {
	EntityManager em = super.getEntityManager();

    	// Get the OpenJPA facade to the given entity manager
    	OpenJPAEntityManager facade = OpenJPAPersistence.cast(em);
    	OpenJPAEntityManagerSPI facadeSPI = (OpenJPAEntityManagerSPI) facade;
    	facadeSPI.setQuerySQLCache(flag);
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

    /**
     * @return the tagDAO
     */
    protected TagDAO getTagDAO() {
	tagDAO.setEntityManager(getEntityManager());
	return tagDAO;
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
	    em.createNativeQuery("select 1").getSingleResult();
	    return true;
	} catch (Exception e) {
	    return false;
	}
    }

}
