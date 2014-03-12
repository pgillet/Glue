package com.glue.api.operations;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.glue.domain.Event;
import com.glue.exceptions.GlueException;

public interface StreamOperations {

    Event createEvent(String title, String description, boolean publicc,
	    boolean open, Set<String> tags,
	    Map<String, String> invitedParticipants,
	    String sharedSecretQuestion, String sharedSecretAnswer,
	    boolean shouldRequestToParticipate, Date startDate, Date endDate,
	    double latitude, double longitude, String address)
	    throws GlueException;

    Event createEvent(Event event) throws GlueException;

    Event updateEvent(Event event) throws GlueException;

    List<Event> searchEvents(String query) throws GlueException;

    void joinStream(String streamID) throws GlueException;

    void joinEvent(Event event) throws GlueException;

}
