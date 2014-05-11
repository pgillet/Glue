package com.glue.api.application;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.glue.api.operations.StreamOperations;
import com.glue.domain.Event;
import com.glue.domain.Tag;
import com.glue.domain.Venue;
import com.glue.exceptions.GlueException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StreamOperationsImpl implements StreamOperations {

    private static final String CREATE_OR_UPDATE_STREAM = "CreateOrUpdateStream";
    private static final String JOIN_STREAM = "JoinStream";
    private static final String SEARCH_STREAM = "SearchStream";
    private final Gson gson = new Gson();

    private GlueClientContext ctx;

    public StreamOperationsImpl(GlueClientContext ctx) {
	this.ctx = ctx;
    }

    @Override
    public Event createEvent(String title, String description, boolean publicc,
	    boolean open, Set<String> tags,
	    Map<String, String> invitedParticipants,
	    String sharedSecretQuestion, String sharedSecretAnswer,
	    boolean shouldRequestToParticipate, Date startDate, Date endDate,
	    double latitude, double longitude, String address)
	    throws GlueException {

	// Create Stream DTO
	Event event = new Event();
	event.setTitle(title);
	event.setDescription(description);

	event.setStartTime(startDate);
	event.setStopTime(endDate);

	Set<Tag> tagObjs = new HashSet<>();
	for (String str : tags) {
	    Tag tag = new Tag();
	    tag.setTitle(str);
	    tagObjs.add(tag);
	}
	event.setTags(tagObjs);

	Venue venue = new Venue();
	venue.setLatitude(latitude);
	venue.setLongitude(longitude);
	venue.setAddress(address);
	event.setVenue(venue);

	return createOrUpdateEvent(event);
    }

    @Override
    public Event createEvent(Event event) throws GlueException {
	return createOrUpdateEvent(event);
    }

    @Override
    public Event updateEvent(Event event) throws GlueException {
	return createOrUpdateEvent(event);
    }

    @Override
    public void joinStream(String streamID) throws GlueException {
	Event event = new Event();
	event.setId(streamID);
	joinEvent(event);
    }

    @Override
    public void joinEvent(Event event) throws GlueException {
	HttpHelper.sendGlueObject(ctx.getHttpClient(), event, Event.class,
		JOIN_STREAM);
    }

    private Event createOrUpdateEvent(Event event) throws GlueException {
	return HttpHelper.sendGlueObject(ctx.getHttpClient(), event,
		Event.class, CREATE_OR_UPDATE_STREAM);
    }

    @Override
    public List<Event> searchEvents(String query) throws GlueException {
	Map<String, String> params = new HashMap<String, String>();
	params.put("query", "query");
	String response = HttpHelper.send(ctx.getHttpClient(), params,
		SEARCH_STREAM);
	return gson.fromJson(response, new TypeToken<List<Event>>() {
	}.getType());
    }
}
