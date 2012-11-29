package com.glue.struct;

/**
 * An interface representing user.
 * 
 */

public interface IUser extends Comparable<IUser>, java.io.Serializable {

	/**
	 * Returns the name of the user.
	 * 
	 * @return the name of the user
	 */
	String getName();

}
