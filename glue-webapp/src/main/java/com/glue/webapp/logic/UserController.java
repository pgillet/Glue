package com.glue.webapp.logic;

import java.sql.SQLException;

import javax.naming.NamingException;

import com.glue.struct.IUser;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.UserDAO;

public class UserController {

	public void createUser(IUser user) throws InternalServerException,
			AlreadyExistsException {
		try {
			DAOManager manager = DAOManager.getInstance();
			UserDAO userDAO = manager.getUserDAO();

			IUser other = userDAO.search(user.getMailAddress());
			if (other != null) {
				throw new AlreadyExistsException("User already exists");
			}

			userDAO.create(user);
		} catch (NamingException e) {
			throw new InternalServerException(e);
		} catch (SQLException e) {
			throw new InternalServerException(e);
		}
	}
}