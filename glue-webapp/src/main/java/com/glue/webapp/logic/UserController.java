package com.glue.webapp.logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.glue.struct.IUser;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.UserDAO;

public class UserController {

	public void createUser(IUser user) throws InternalServerException,
			AlreadyExistsException {
		DAOManager manager = null;

		try {
			manager = DAOManager.getInstance();
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
		} finally {
			if (manager != null) {
				manager.closeConnectionQuietly();
			}
		}
	}

	public void updateUser(IUser user) throws InternalServerException {
		DAOManager manager = null;

		try {
			manager = DAOManager.getInstance();
			UserDAO userDAO = manager.getUserDAO();
			userDAO.update(user);
		} catch (NamingException e) {
			throw new InternalServerException(e);
		} catch (SQLException e) {
			throw new InternalServerException(e);
		} finally {
			if (manager != null) {
				manager.closeConnectionQuietly();
			}
		}
	}

	public IUser getUser(String userId) throws InternalServerException {
		IUser user = null;
		DAOManager manager = null;

		try {
			manager = DAOManager.getInstance();
			UserDAO userDAO = manager.getUserDAO();

			user = userDAO.search(Long.valueOf(userId));
		} catch (NamingException e) {
			throw new InternalServerException(e);
		} catch (SQLException e) {
			throw new InternalServerException(e);
		} finally {
			if (manager != null) {
				manager.closeConnectionQuietly();
			}
		}

		return user;
	}

	/**
	 * Returns a list of users from the given email addresses. A registered user
	 * might not exist for every address.
	 * 
	 * @param mailingList
	 * @return
	 * @throws InternalServerException
	 */
	public List<IUser> getUsers(String[] addresses)
			throws InternalServerException {
		List<IUser> users = new ArrayList<IUser>();
		DAOManager manager = null;

		try {
			manager = DAOManager.getInstance();
			UserDAO userDAO = manager.getUserDAO();

			for (String address : addresses) {
				IUser user = userDAO.search(address);
				if (user != null) {
					// Registered user
					users.add(user);
				}
			}
		} catch (NamingException e) {
			throw new InternalServerException(e);
		} catch (SQLException e) {
			throw new InternalServerException(e);
		} finally {
			if (manager != null) {
				manager.closeConnectionQuietly();
			}
		}

		return users;
	}

}