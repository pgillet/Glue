package com.glue.webapp.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.glue.domain.Event;
import com.glue.persistence.GluePersistenceService;
import com.glue.webapp.logic.EventController;
import com.glue.webapp.logic.InternalServerException;

@Path("/events")
public class EventResource {
	
    @Inject
    EventController controller;
	
	@GET @Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Event find(@PathParam("id") String id) {
		Event event = new Event();
		try {
			event = controller.search(id);
		} catch (InternalServerException e) {
			e.printStackTrace();
		}
		return event;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Event create(Event event) {
		Event result = null;
		try {
			controller.create(event);
		} catch (InternalServerException e) {
			e.printStackTrace();
		}
		return result;
	}
}
