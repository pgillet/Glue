package com.glue.persistence;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;

import com.glue.domain.User;

@NamedQueries({
	@NamedQuery(name = "findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
	@NamedQuery(name = "findByCredentials", query = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password") })
public class UserDAO extends AbstractDAO<User> {

    public UserDAO() {
	super();
    }

    public User authenticate(String email, String password) {
	Query query = em.createNamedQuery("findByCredentials", type)
		.setParameter("email", email)
		.setParameter("password", password);

	User user = PersistenceHelper.getSingleResultOrNull(query);

	return user;
    }

    public User findByEmail(String email) {
	Query query = em.createNamedQuery("findByEmail", type).setParameter(
		"email", email);

	User user = PersistenceHelper.getSingleResultOrNull(query);

	return user;
    }

}
