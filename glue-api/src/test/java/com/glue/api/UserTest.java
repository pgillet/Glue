package com.glue.api;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.struct.IUser;

public class UserTest {

	Glue glue;
	IUser user;

	@Before
	public void setUp() throws Exception {
		glue = new GlueFactory().getInstance();
	}

	@Test
	public void createUser1() {
		try {
			user = glue.createUser("Greg", "Denis", "gregoire.denis@glue.com", "mypassword");
			assertTrue(user.getId() != 0);
			user.setFirstName("Gregouze");
			user = glue.updateUser(user);
		} catch (Exception e) {
			fail("Exception during stream creation");
		}

	}

}
