package com.glue.domain;

/**
 * An interface representing user.
 * 
 */

public interface IUser {

	long getId();

	void setId(long id);

	/**
	 * Returns the user's name.
	 * 
	 * @return the user's name
	 */
	String getName();

	void setName(String name);

	/**
	 * Returns the user email.
	 * 
	 * @return the
	 */
	String getMailAddress();

	void setMailAddress(String mail);

	/**
	 * Returns the user password.
	 * 
	 * @return the
	 */
	String getPassword();

	void setPassword(String password);

}
