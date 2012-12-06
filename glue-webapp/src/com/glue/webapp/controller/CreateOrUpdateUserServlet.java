package com.glue.webapp.controller;

import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;

import com.glue.struct.IUser;
import com.glue.webapp.db.UserDAO;

/**
 * Create or update user.
 */
@WebServlet("/CreateOrUpdateUser")
public class CreateOrUpdateUserServlet extends AbstractUserServlet {

	private static final long serialVersionUID = 4254630038354914304L;

	@Override
	protected void doOperation(IUser user) throws SQLException {

		// Create or update User
		UserDAO userDAO = new UserDAO(connection);

		// I'm looking for an existing user
		if (userDAO.search(user.getId()) != null) {
			userDAO.update(user);
		} else {
			userDAO.create(user);
		}

	}
}
