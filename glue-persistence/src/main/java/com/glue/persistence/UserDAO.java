package com.glue.persistence;

import javax.persistence.Query;

import com.glue.domain.User;

public class UserDAO extends AbstractDAO<User> {

    public UserDAO() {
	super();
    }

    public User authenticate(String email, String password) {

	final String queryString = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password";

	Query query = em.createQuery(queryString, type)
		.setParameter("email", email)
		.setParameter("password", password);

	User user = PersistenceHelper.getSingleResultOrNull(query);

	return user;
    }

    public User findByEmail(String email) {

	final String queryString = "SELECT u FROM User u WHERE u.email = :email";

	Query query = em.createQuery(queryString, type).setParameter("email",
		email);

	User user = PersistenceHelper.getSingleResultOrNull(query);

	return user;
    }

}
