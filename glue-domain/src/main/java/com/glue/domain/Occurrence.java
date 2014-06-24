package com.glue.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * A class that defines an occurrence of an event. An event can be recurrent and
 * an occurrence herein refers to an actual instance where an event occurs in
 * time and / or space (e.g. a representation of a play, a concert in a band's
 * tour).
 * 
 * @author pgillet
 * 
 */
@Entity
public class Occurrence {

    /**
     * Occurrence ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * Event start time.
     */
    @Column(nullable = false)
    private Date startTime;

    private Date stopTime;

    @ManyToOne(cascade = { CascadeType.DETACH, /* CascadeType.PERSIST, */
    CascadeType.REFRESH, CascadeType.MERGE }, optional = false)
    private Venue venue;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public Date getStartTime() {
	return startTime;
    }

    public void setStartTime(Date startTime) {
	this.startTime = startTime;
    }

    public Date getStopTime() {
	return stopTime;
    }

    public void setStopTime(Date stopTime) {
	this.stopTime = stopTime;
    }

    public Venue getVenue() {
	return venue;
    }

    public void setVenue(Venue venue) {
	this.venue = venue;
    }

}
