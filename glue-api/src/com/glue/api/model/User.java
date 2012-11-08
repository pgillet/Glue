package com.glue.api.model;

/**
 * An interface representing user.
 * 
 */

public interface User extends Comparable<User>, java.io.Serializable {

	/**
	 * Returns the name of the user.
	 * 
	 * @return the name of the user
	 */
	String getName();

}
