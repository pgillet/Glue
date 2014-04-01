package com.glue.webapp.services;

import java.net.URI;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.User;
import com.glue.webapp.logic.AlreadyExistsException;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.logic.UserController;

@ManagedBean
@Path("/users")
public class UserResource {

    static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    @Context
    SecurityContext securityContext;

    @Context
    UriInfo uriInfo;

    @Inject
    UserController userController;

    public UserResource() {
    }

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {

	try {
	    userController.createUser(user);
	} catch (InternalServerException e) {
	    LOG.error(e.getMessage(), e);
	    throw new WebApplicationException(e,
		    Response.Status.INTERNAL_SERVER_ERROR);
	} catch (AlreadyExistsException e) {
	    LOG.error(e.getMessage(), e);
	    throw new WebApplicationException(e, Response
		    .status(Response.Status.CONFLICT).entity(e.getMessage())
		    /*
		     * .type(MediaType. TEXT_PLAIN)
		     */.build());
	}

	UriBuilder ub = uriInfo.getAbsolutePathBuilder();
	URI userUri = ub.path(String.valueOf(user.getId())).build();

	return Response.created(userUri).entity(user).build();
    }

    @POST
    @Path("/{userid}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("userid") String userId, User user) {

	User authenticatedUser = (User) securityContext.getUserPrincipal();

	if (authenticatedUser != null
		&& userId.equals(authenticatedUser.getId())) {

	    try {
		user.setId(userId);
		// TODO: should encrypt password
		userController.updateUser(user);

		return Response.ok().build();
	    } catch (InternalServerException e) {
		LOG.error(e.getMessage(), e);
		throw new WebApplicationException(e,
			Response.Status.INTERNAL_SERVER_ERROR);
	    }
	}

	return Response.status(Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{userid}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public User getUser(@PathParam("userid") String userId) {
	try {
	    User user = (User) userController.getUser(userId);
	    // Password obfuscation
	    user.setPassword("***"); // TODO: should be hidden ahead ?

	    return user;
	} catch (InternalServerException e) {
	    LOG.error(e.getMessage(), e);
	    throw new WebApplicationException(e,
		    Response.Status.INTERNAL_SERVER_ERROR);
	}
    }

}
