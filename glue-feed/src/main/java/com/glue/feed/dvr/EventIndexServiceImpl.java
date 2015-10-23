package com.glue.feed.dvr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.solr.client.solrj.SolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.Event_;
import com.glue.persistence.GluePersistenceService;
import com.glue.persistence.index.SolrServerManager;

public class EventIndexServiceImpl extends GluePersistenceService implements
	EventIndexService {

    static final Logger LOG = LoggerFactory
	    .getLogger(EventIndexServiceImpl.class);

    @Override
    public List<String> getWithdrawnEventIds() {

	EntityManager em = getEntityManager();

	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<String> cq = cb.createQuery(String.class);
	Root<Event> event = cq.from(Event.class);

	cq.select(event.get(Event_.id));

	List<Predicate> conjunction = new ArrayList<>();

	conjunction.add(cb.isTrue(event.get(Event_.withdrawn)));

	cq.where(conjunction.toArray(new Predicate[conjunction.size()]));

	List<String> ids = em.createQuery(cq).getResultList();

	return ids;
    }

    @Override
    public void deleteById(List<String> ids) throws Exception {
	// Explicit Solr operation
	LOG.debug("Deletes from the index a list of documents by unique ID");
	SolrServer solrServer = SolrServerManager.getSolrServer();
	solrServer.deleteById(ids);
	// Commit
	solrServer.commit();
    }

}
