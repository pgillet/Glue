package com.glue.webapp.services;

import java.net.URI;

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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.glue.struct.impl.User;
import com.glue.webapp.logic.AlreadyExistsException;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.logic.UserController;

@Path("/users")
public class UserResource {

	@Context
	UriInfo uriInfo;

	// TODO: should probably use Dependency Injection here!
	UserController userController = new UserController();

	public UserResource() {
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(User user) {

		try {
			userController.createUser(user);
		} catch (InternalServerException e) {
			throw new WebApplicationException(
					Response.Status.INTERNAL_SERVER_ERROR);
		} catch (AlreadyExistsException e) {
			throw new WebApplicationException(Response
					.status(Response.Status.CONFLICT).entity(e.getMessage())
					/*
					 * .type(MediaType. TEXT_PLAIN)
					 */.build());
		}

		UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		URI userUri = ub.path(String.valueOf(user.getId())).build();

		return Response.created(userUri)/* .entity(user) */.build();
	}

	@POST
	@Path("/{userid}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("userid") String userId, User user) {
		try {
			user.setId(Long.valueOf(userId));
			userController.updateUser(user);
		} catch (InternalServerException e) {
			throw new WebApplicationException(
					Response.Status.INTERNAL_SERVER_ERROR);
		}

		return Response.ok().build();
	}

	@GET
	@Path("/{userid}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public User getUser(@PathParam("userid") String userId) {
		try {
			User user = (User) userController.getUser(userId);
			// Password obfuscation
			user.setPassword("***"); // TODO: should be hidden ahead ?

			return user;
		} catch (InternalServerException e) {
			throw new WebApplicationException(
					Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

}
