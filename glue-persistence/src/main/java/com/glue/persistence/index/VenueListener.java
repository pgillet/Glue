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
import com.glue.domain.Venue;
import com.glue.persistence.EntityListenerRealm;
import com.glue.persistence.TransactionListenerRealm;

public class VenueListener extends BaseSolrEntityIndexer {

    static final Logger LOG = LoggerFactory.getLogger(VenueListener.class);

    private SolrDocumentFactory sdf = new SolrDocumentFactory();

    /**
     * The collection of pending documents to be added.
     */
    private List<SolrInputDocument> pendingDocuments = new ArrayList<>();

    public VenueListener() {
	super();
	// Registers itself
	EntityListenerRealm.add(this);
	TransactionListenerRealm.add(this);
    }

    // @PostUpdate
    public void update(Object pc) throws Exception {
	Venue venue = (Venue) pc;

	Venue other = venue.getParent();

	if (other != null && other.isReference()) {
	    // Update all event documents for this venue with the lat/long,
	    // venue name and city of the reference venue.
	    List<Event> events = venue.getEvents();

	    // Is it better to retrieve documents from Solr and make partial
	    // updates or overwrite them by adding full documents?
	    // See
	    // http://stackoverflow.com/questions/8472948/search-document-by-id-very-slow

	    for (Event event : events) {
		SolrInputDocument doc = sdf.createDocument(event);
		pendingDocuments.add(doc);
	    }
	}

    }

    @Override
    public void afterCommit(TransactionEvent event) {

	try {
	    for (SolrInputDocument doc : pendingDocuments) {
		addDoc(doc);
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
    }

}
