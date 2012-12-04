package com.glue.api;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.exceptions.GlueException;
import com.glue.struct.IStream;

public class CreateStream {

	Glue glue;
	IStream aSream;

	@Before
	public void setUp() throws Exception {
		glue = new GlueFactory().getInstance();
	}

	@Test
	public void createAnEmptyStream() {
		try {
			aSream = glue.createStream("Concert Nirvana", "Concert au bikini, lundi 9 décembre", true, true, null,
					null, null, false, new Date().getTime(), new Date().getTime(), 10.255, 15.378, null);
		} catch (GlueException e) {
			fail("Exception during stream creation");
		}
	}

	@Test
	public void createStreamWithInvitedParticpant() {
		try {
			Map<String, String> ipList = new HashMap<String, String>();
			ipList.put("greg@glue.com", "Name");
			ipList.put("pascal@glue.com", "Name");
			aSream = glue.createStream("Concert Cypress Hill", null, true, true, ipList, null, null, false,
					new Date().getTime(), 0, 10.255, 15.378, null);
		} catch (GlueException e) {
			fail("Exception during stream creation");
		}
	}
}
