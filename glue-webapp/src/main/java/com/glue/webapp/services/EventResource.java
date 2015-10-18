package com.glue.webapp.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.glue.domain.Event;
import com.glue.webapp.logic.EventController;
import com.glue.webapp.logic.InternalServerException;

@Path("/events")
public class EventResource {

    @Inject
    EventController controller;

    @GET
    @Path("{id}")
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
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Event create(@FormDataParam("username") String param,
	    @FormDataParam("file") InputStream image) {
	System.out.println(param);
	Event event = null;
	try {
	    FileUtils.copyInputStreamToFile(image, new File("D:\\test.jpg"));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	// try {
	// // event = new ObjectMapper().readValue(param, Event.class);
	// event = controller.create(event);
	// } catch (InternalServerException e) {
	// e.printStackTrace();
	// }
	return event;
    }
}
