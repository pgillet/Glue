package com.glue.catalog.domain;

import java.util.Date;

import javax.persistence.ManyToOne;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.glue.bot.domain.EventSelectors;
import com.glue.bot.domain.SiteMap;
import com.glue.catalog.security.Manager;
import com.glue.domain.Event;


public class EventWebsite {

    @Id
    private String id;
    private String uri;
    private boolean activated;
    // java.time classes not yet supported when serialized in Json
    private Date lastVisited;
    private Frequency crawlFrequency = Frequency.MONTHLY;
    private SiteMap siteMap;
    private EventSelectors eventSelectors;
    // @ReadOnlyProperty
    private @ManyToOne Manager manager;

    private @Version @JsonIgnore Long version;

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
    
    public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public Date getLastVisited() {
		return lastVisited;
	}

	public void setLastVisited(Date lastVisited) {
		this.lastVisited = lastVisited;
	}

	public Frequency getCrawlFrequency() {
		return crawlFrequency;
	}

	public void setCrawlFrequency(Frequency crawlFrequency) {
		this.crawlFrequency = crawlFrequency;
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
