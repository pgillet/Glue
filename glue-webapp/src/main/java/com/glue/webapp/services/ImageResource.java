package com.glue.webapp.services;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.content.ContentManager;
import com.glue.domain.Event;
import com.glue.domain.Image;
import com.glue.persistence.EventDAO;

@Path("/images")
public class ImageResource {

    static final Logger LOG = LoggerFactory.getLogger(ImageResource.class);

    @Inject
    private EventDAO eventDAO;

    @Inject
    private ContentManager cm;

    @GET
    @Path("/thumbnails/{eventId}")
    @Produces("image/*")
    public Response getImage(@PathParam("eventId") String eventId) {

	Event event = eventDAO.findWithImages(eventId);

	Image image = null;
	Iterator<Image> iter = event.getImages().iterator();
	while (iter.hasNext()) {
	    Image img = iter.next();
	    if (img.isSticky()) {
		image = img;
		break;
	    }
	}

	if (image != null) {
	    try {
		URL url = new URL(image.getOriginal().getUrl());

		// TODO: to reduce server's memory and IO bandwidth, much better
		// to delegate that task to a proper web server that is
		// optimized for this kind of transfer. This can be accomplished
		// by sending a redirect to the image resource (as a HTTP 302
		// response with the CMIS ATOM Pub URI of the image).

		String name = FilenameUtils.getName(url.getPath());
		InputStream in = cm.getEventCAO().getDocument(name, event);

		if (in != null) {
		    return Response.ok(in, MediaType.APPLICATION_OCTET_STREAM)
			    .build();
		} else {
		    // Redirection
		    return Response.seeOther(url.toURI()).build();
		}
	    } catch (MalformedURLException e) {
		LOG.error(e.getMessage(), e);
	    } catch (URISyntaxException e) {
		LOG.error(e.getMessage(), e);
	    }
	}

	// HTTP Status: 204 No Content
	// The server successfully processed the request, but is not returning
	// any content
	return Response.noContent().build();
    }

}
