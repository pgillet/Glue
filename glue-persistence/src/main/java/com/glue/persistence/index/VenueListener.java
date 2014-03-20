package com.glue.persistence.index;

import java.util.List;

import org.apache.solr.common.SolrInputDocument;

import com.glue.domain.Event;
import com.glue.domain.Venue;
import com.glue.persistence.EntityListenerRealm;

public class VenueListener extends BaseSolrEntityIndexer {

    private SolrDocumentFactory sdf = new SolrDocumentFactory();

    public VenueListener() {
	super();
	// Registers itself
	EntityListenerRealm.add(this);
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
		addDoc(doc);
	    }
	}

    }

}
