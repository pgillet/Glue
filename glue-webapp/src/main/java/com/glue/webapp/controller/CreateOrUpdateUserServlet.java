package com.glue.webapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.glue.struct.IUser;
import com.glue.struct.impl.User;
import com.glue.webapp.db.UserDAO;
import com.glue.webapp.utilities.GSonHelper;

/**
 * Create or update user.
 */
@WebServlet("/CreateOrUpdateUser")
public class CreateOrUpdateUserServlet extends AbstractDatabaseServlet {

	private static final long serialVersionUID = 4254630038354914304L;
	private IUser user;

	@Override
	protected void doOperation() throws SQLException {

		// Create or update User
		UserDAO userDAO = new UserDAO(connection);

		// I'm looking for an existing user
		if (userDAO.search(user.getId()) != null) {
			userDAO.update(user);
		} else {
			userDAO.create(user);
		}

	}

	@Override
	protected void retrieveDatasFromRequest(HttpServletRequest request) throws IOException {
		user = GSonHelper.getGsonObjectFromRequest(request, User.class);
	}

	@Override
	protected void sendResponse(HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		writer.write(gson.toJson(user));
	}

	@Override
	protected boolean isUserAuthorized(Connection connection) throws SQLException {
		return true;
	}
}
