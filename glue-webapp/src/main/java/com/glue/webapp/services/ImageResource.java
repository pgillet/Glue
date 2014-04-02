package com.glue.webapp.services;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.persistence.EventDAO;
import com.glue.webapp.beans.EventUtilBean;

@Path("/images")
public class ImageResource {

    static final Logger LOG = LoggerFactory.getLogger(ImageResource.class);

    @Inject
    private EventDAO eventDAO;

    @Inject
    private EventUtilBean eventUtilBean;

    @GET
    @Path("/thumbnails/{eventId}")
    @Produces("image/*")
    public Response getImage(@PathParam("eventId") String eventId) {

	Event event = eventDAO.findWithImages(eventId);
	String str = eventUtilBean.getStickyImageURI(event);

	if (str != null) {
	    // Redirection
	    return Response.seeOther(URI.create(str)).build();
	}

	// HTTP Status: 204 No Content
	// The server successfully processed the request, but is not returning
	// any content
	return Response.noContent().build();
    }

}
