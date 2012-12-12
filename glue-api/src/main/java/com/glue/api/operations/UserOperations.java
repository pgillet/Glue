package com.glue.api.operations;

import com.glue.exceptions.GlueException;
import com.glue.struct.IUser;

public interface UserOperations {
	IUser createUser(IUser user) throws GlueException;

	IUser createUser(String firstName, String lastName, String email, String password) throws GlueException;

	IUser updateUser(IUser user) throws GlueException;

	void login(String username, String password) throws GlueException;

	void logout() throws GlueException;

}
