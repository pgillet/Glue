package com.glue.webapp.services;

import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.glue.struct.impl.User;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.UserDAO;

//@Path("/users/{username}")
@Path("/user")
public class UserResource {

	// @Resource(name = "jdbc/gluedb")
	DataSource dataSource;

	public UserResource() throws NamingException {
		// Obtain Connection
		InitialContext initialContext = new InitialContext();
		dataSource = (DataSource) initialContext
				.lookup("java:comp/env/jdbc/gluedb");
	}

	// CRUD

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public User createUser(User user) {

		DAOManager manager = DAOManager.getInstance(dataSource);
		try {
			UserDAO userDAO = manager.getUserDAO();

			// I'm looking for an existing user
			if (userDAO.search(user.getId()) != null) {
				userDAO.update(user);
			} else {
				userDAO.create(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;
	}

	// @GET
	// // @Produces("application/json")
	// @Produces("text/plain")
	// public String getUser(@PathParam("username") String userName) {
	// return userName;
	// }
}
