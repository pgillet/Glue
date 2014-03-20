package com.glue.persistence.index;

import org.apache.solr.common.SolrInputDocument;

import com.glue.domain.Event;
import com.glue.persistence.EntityListenerRealm;

public class EventListener extends BaseSolrEntityIndexer {

    private SolrDocumentFactory sdf = new SolrDocumentFactory();

    public EventListener() {
	super();
	// Registers itself
	EntityListenerRealm.add(this);
    }

    // @PostPersist
    public void add(Object pc) throws Exception {

	Event event = (Event) pc;
	SolrInputDocument doc = sdf.createDocument(event);

	addDoc(doc);
    }

    // @PreRemove
    public void delete(Object pc) throws Exception {
	getSolrServer().deleteById(((Event) pc).getId());
	getSolrServer().commit();
    }

    // @PostUpdate
    public void update(Object pc) throws Exception {
	add(pc);
    }

}
