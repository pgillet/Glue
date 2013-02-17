package com.glue.struct;

/**
 * An interface representing user.
 * 
 */

public interface IUser {

	long getId();

	void setId(long id);

	/**
	 * Returns the first name of the user.
	 * 
	 * @return the first name of the user
	 */
	String getFirstName();

	void setFirstName(String firstName);

	/**
	 * Returns the last name of the user.
	 * 
	 * @return the last name of the user
	 */
	String getLastName();

	void setLastName(String lastName);

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
