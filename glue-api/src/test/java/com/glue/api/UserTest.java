package com.glue.api;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.domain.User;

public class UserTest {

    Glue glue;
    User user;

    @Before
    public void setUp() throws Exception {
	glue = new GlueFactory().getInstance();
    }

    @Test
    public void createUser1() {
	try {
	    final String email = "gregoire.denis@glue.com";
	    final String passwd = "mypassword";
	    user = glue.createUser("Greg", email, passwd);
	    assertTrue(user.getId() != null);
	    user.setUsername("Gregouze");
	    glue.login(email, passwd);
	    glue.updateUser(user);
	} catch (Exception e) {
	    fail("Exception during user creation");
	}

    }

}
