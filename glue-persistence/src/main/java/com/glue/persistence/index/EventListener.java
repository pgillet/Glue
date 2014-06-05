package com.glue.persistence.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.openjpa.event.TransactionEvent;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.persistence.EntityListenerRealm;
import com.glue.persistence.TransactionListenerRealm;

public class EventListener extends BaseSolrEntityIndexer {

    static final Logger LOG = LoggerFactory.getLogger(EventListener.class);

    private SolrDocumentFactory sdf = new SolrDocumentFactory();

    /**
     * The collection of pending documents to be added.
     */
    private List<SolrInputDocument> pendingDocuments = new ArrayList<>();

    /**
     * The list of document IDs to delete.
     */
    private List<String> ids = new ArrayList<>();

    public EventListener() {
	super();
	// Registers itself
	EntityListenerRealm.add(this);
	TransactionListenerRealm.add(this);
    }

    // @PostPersist
    public void add(Object pc) throws Exception {

	Event event = (Event) pc;
	SolrInputDocument doc = sdf.createDocument(event);

	pendingDocuments.add(doc);
    }

    // @PreRemove
    public void delete(Object pc) throws Exception {
	Event event = (Event) pc;
	ids.add(event.getId());
    }

    // @PostUpdate
    public void update(Object pc) throws Exception {
	add(pc);
    }

    @Override
    public void afterCommit(TransactionEvent event) {

	try {
	    for (SolrInputDocument doc : pendingDocuments) {
		addDoc(doc);
	    }

	    if (!ids.isEmpty()) {
		getSolrServer().deleteById(ids);
		getSolrServer().commit();
	    }

	} catch (SolrServerException | IOException e) {
	    LOG.error(e.getMessage(), e);
	    throw new RuntimeException(e);
	} finally {
	    clearTransactionalState();
	}

    }

    @Override
    public void afterRollback(TransactionEvent event) {
	clearTransactionalState();
    }

    /**
     * Clear state.
     */
    private void clearTransactionalState() {
	pendingDocuments.clear();
	ids.clear();
    }

}
