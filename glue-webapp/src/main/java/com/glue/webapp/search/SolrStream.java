package com.glue.webapp.search;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

import com.glue.domain.Event;

/**
 * A technical implementation of an Event that adds @Field annotations allowing
 * to read Documents returned by Solr directly as beans.
 * 
 * @author pgillet
 * 
 */
public class SolrStream extends Event {

    @Field
    private String id;

    @Field
    private String title;

    @Field
    private String description;

    @Field
    private Date startTime;

    @Field
    private Date stopTime;

    @Override
    public String getId() {
	return id;
    }

    @Override
    public void setId(String id) {
	this.id = id;
    }

    @Override
    public String getTitle() {
	return title;
    }

    @Override
    public void setTitle(String title) {
	this.title = title;
    }

    @Override
    public String getDescription() {
	return description;
    }

    @Override
    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public Date getStartTime() {
	return startTime;
    }

    @Override
    public void setStartTime(Date startTime) {
	this.startTime = startTime;
    }


    @Override
    public Date getStopTime() {
	return stopTime;
    }

    @Override
    public void setStopTime(Date stopTime) {
	this.stopTime = stopTime;
    }

}
