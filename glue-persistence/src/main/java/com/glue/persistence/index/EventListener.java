package com.glue.persistence.index;

import org.apache.solr.common.SolrInputDocument;

import com.glue.domain.Event;

public class EventListener extends BaseSolrEntityIndexer {

    public static final String FIELD_LONGITUDE = "latlng_1_coordinate";
    public static final String FIELD_LATITUDE = "latlng_0_coordinate";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_VENUE = "venue";
    public static final String FIELD_START_TIME = "startTime";
    public static final String FIELD_STOP_TIME = "stopTime";
    public static final String FIELD_DURATION = "duration";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_ID = "id";

    public EventListener() {
	super();
    }

    // @PostPersist
    public void add(Object pc) throws Exception {

	Event event = (Event) pc;

	SolrInputDocument doc = new SolrInputDocument();

	// See also SolrInputDocument#addField(String name, Object value,float
	// boost)
	doc.addField(FIELD_ID, event.getId());
	doc.addField(FIELD_TITLE, event.getTitle());
	doc.addField(FIELD_CATEGORY, event.getCategory().getName());
	doc.addField(FIELD_DESCRIPTION, event.getDescription());
	doc.addField(FIELD_START_TIME, event.getStartTime());
	doc.addField(FIELD_STOP_TIME, event.getStopTime());
	// Duration
	doc.addField(FIELD_DURATION, (event.getStopTime().getTime() - event
		.getStartTime().getTime()) * 1000);
	doc.addField(FIELD_VENUE, event.getVenue().getName());
	doc.addField(FIELD_CITY, event.getVenue().getCity());
	doc.addField(FIELD_LATITUDE, event.getVenue().getLatitude());
	doc.addField(FIELD_LONGITUDE, event.getVenue().getLongitude());

	addDoc(doc);
    }

    // @PreRemove
    public void delete(Object pc) throws Exception {
	getSolrServer().deleteById(((Event) pc).getId());
	getSolrServer().commit();
    }

    // @PostUpdate
    public void update(Event aEvent) throws Exception {
	// TODO ...
    }

}
