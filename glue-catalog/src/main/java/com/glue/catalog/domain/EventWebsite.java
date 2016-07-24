package com.glue.catalog.domain;

import javax.persistence.ManyToOne;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.glue.catalog.security.Manager;
import com.glue.domain.Event;


public class EventWebsite {

    @Id
    private String id;
    private String uri;
    private SiteMap siteMap;
    private EventSelectors eventSelectors;
    // @ReadOnlyProperty
    private @ManyToOne Manager manager;

    private @Version @JsonIgnore Long version;

    @JsonIgnore
    private Event eventTemplate;

    public EventWebsite() {
    }

    public EventWebsite(String uri) {
	this.uri = uri;
    }

    public String getUri() {
	return uri;
    }

    public void setUri(String uri) {
	this.uri = uri;
    }

    public Manager getManager() {
	return manager;
    }

    public void setManager(Manager manager) {
	this.manager = manager;
    }

    public SiteMap getSiteMap() {
	return siteMap;
    }

    public void setSiteMap(SiteMap siteMap) {
	this.siteMap = siteMap;
    }

    public EventSelectors getEventSelectors() {
	return eventSelectors;
    }

    public void setEventSelectors(EventSelectors eventSelectors) {
	this.eventSelectors = eventSelectors;
    }

    public Event getEventTemplate() {
	return eventTemplate;
    }

    public void setEventTemplate(Event eventTemplate) {
	this.eventTemplate = eventTemplate;
    }

    @Override
    public String toString() {
	return "EventWebsite [uri=" + uri + ", siteMap=" + siteMap
		+ ", eventSelectors=" + eventSelectors + ", eventTemplate="
		+ eventTemplate + "]";
    }

    
}