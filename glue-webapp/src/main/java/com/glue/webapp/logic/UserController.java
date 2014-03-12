package com.glue.webapp.logic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.User;
import com.glue.persistence.UserDAO;

public class UserController {

    static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Inject
    private UserDAO userDAO;

    public void createUser(User user) throws InternalServerException,
	    AlreadyExistsException {

	try {

	    User other = userDAO.findByEmail(user.getEmail());
	    if (other != null) {
		throw new AlreadyExistsException("User already exists");
	    }

	    userDAO.create(user);

	} finally {
	}
    }

    public void updateUser(User user) throws InternalServerException {
	try {
	    userDAO.update(user);
	} finally {
	}
    }

    public User getUser(String userId) throws InternalServerException {
	User user = null;

	try {
	    user = userDAO.find(userId);
	} finally {
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
    public List<User> getUsers(String[] addresses)
	    throws InternalServerException {
	List<User> users = new ArrayList<User>();

	try {
	    for (String address : addresses) {
		User user = userDAO.findByEmail(address);
		if (user != null) {
		    // Registered user
		    users.add(user);
		}
	    }
	} finally {
	}

	return users;
    }

}