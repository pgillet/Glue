package com.glue.persistence.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.Event_;
import com.glue.persistence.EventDAO;
import com.glue.persistence.GluePersistenceService;

/**
 * This service is intended to be used for data import instead of the Solr DIH
 * from the admin interface.
 * 
 * <p>
 * Solr DIH does not support indexing for hierarchical documents yet. This
 * service will be obsolete with the upcoming Solr release 4.9, and the DIH may
 * be used at this time.
 * </p>
 * 
 * @author pgillet
 * @see https://issues.apache.org/jira/browse/SOLR-5147
 */
public class FullIndexerService extends GluePersistenceService {

    static final Logger LOG = LoggerFactory.getLogger(FullIndexerService.class);

    public FullIndexerService() {
	super();
    }

    public List<Event> next(int start, int rows) {

	EntityManager em = getEntityManager();

	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Event> cq = cb.createQuery(Event.class);
	Root<Event> event = cq.from(Event.class);

	event.fetch(Event_.venue);
	event.fetch(Event_.tags.getName(), JoinType.LEFT);
	event.fetch(Event_.occurrences.getName(), JoinType.LEFT);

	cq.select(event).distinct(true);

	// TODO: Events that are not withdrawn...

	TypedQuery<Event> q = em.createQuery(cq).setFirstResult(start)
		.setMaxResults(rows);
	List<Event> events = q.getResultList();

	return events;
    }

    public static void main(String[] args) throws SolrServerException,
	    IOException {

	long start = System.currentTimeMillis();

	FullIndexerService svc = new FullIndexerService();
	SolrDocumentFactory sdf = new SolrDocumentFactory();
	SolrServer solrServer = SolrServerManager.getSolrServer();
	EventDAO eventDAO = svc.getEventDAO();

	final long count = eventDAO.countAll();
	final int rows = 100;
	int startPos = 0;

	// LOG.info("Deleting all the data from the index...");
	// solrServer.deleteByQuery("*:*"); // CAUTION: deletes everything!
	LOG.info("*** CAUTION: should delete all the data before full re-index...");
	
	LOG.info("Total event count = " + count);

	while (startPos < count) {
	    LOG.info(startPos + " objects");
	    List<Event> events = svc.next(startPos, rows);
	    List<SolrInputDocument> docs = new ArrayList<>();
	    
	    for (Event event : events) {
		SolrInputDocument doc = sdf.createDocument(event);
		docs.add(doc);
	    }

	    solrServer.add(docs);
	    solrServer.commit();
	    startPos += rows;
	}

	long end = System.currentTimeMillis();
	LOG.info(String.format("Indexed %d events in %d s", count,
		(end - start) / 1000));


    }

}
