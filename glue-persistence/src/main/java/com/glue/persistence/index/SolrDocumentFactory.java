package com.glue.persistence.index;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;

import com.glue.domain.Event;
import com.glue.domain.Tag;
import com.glue.domain.Venue;

public class SolrDocumentFactory {

    public static final String FIELD_LONGITUDE = "latlng_1_coordinate";
    public static final String FIELD_LATITUDE = "latlng_0_coordinate";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_VENUE = "venue";
    public static final String FIELD_START_TIME = "startTime";
    public static final String FIELD_STOP_TIME = "stopTime";
    public static final String FIELD_DURATION = "duration";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_TAGS = "tags";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_ID = "id";

    public SolrInputDocument createDocument(Event event) {

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
	long duration = event.getStopTime() != null ? ((event.getStopTime()
		.getTime() - event.getStartTime().getTime()) * 1000) : 0;
	doc.addField(FIELD_DURATION, duration);

	Venue venue = event.getVenue();
	Venue parent = venue.getParent();
	if (parent != null && parent.isReference()) {
	    venue = parent;
	}

	doc.addField(FIELD_VENUE, venue.getName());
	doc.addField(FIELD_CITY, venue.getCity());
	doc.addField(FIELD_LATITUDE, venue.getLatitude());
	doc.addField(FIELD_LONGITUDE, venue.getLongitude());

	// Tags
	List<String> tags = new ArrayList<>();
	for (Tag tag : event.getTags()) {
	    tags.add(tag.getTitle());
	}
	doc.addField(FIELD_TAGS, tags);

	return doc;
    }

}
