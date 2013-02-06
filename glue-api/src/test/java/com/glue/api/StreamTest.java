package com.glue.api;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.exceptions.GlueException;
import com.glue.struct.IStream;
import com.glue.struct.impl.Stream;

public class StreamTest {

	Glue glue;
	IStream aStream;

	@Before
	public void setUp() throws Exception {
		glue = new GlueFactory().getInstance();
	}

	@Test
	public void createAnEmptyStream() {
		try {
			aStream = glue.createStream("Concert Nirvana", "Concert au bikini, lundi 9 decembre", true, true, null,
					null, null, null, false, new Date().getTime(), new Date().getTime(), 10.255, 15.378, null);
		} catch (GlueException e) {
			fail("Exception during stream creation");
		}
	}

	@Test
	public void createAndUpdate() {
		try {
			aStream = (Stream) glue.createStream("Stream", null, true, true, null, null, null, null, false,
					new Date().getTime(), new Date().getTime(), 10.255, 15.378, null);
			((Stream) aStream).setDescription("Update desc");
			((Stream) aStream).setSharedSecretQuestion("Favorite bannd?");
			aStream = (Stream) glue.updateStream(aStream);
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
			aStream = (Stream) glue.createStream("Concert Cypress Hill", null, true, true, null, ipList, null, null,
					false, new Date().getTime(), 0, 10.255, 15.378, null);
		} catch (GlueException e) {
			fail("Exception during stream creation");
		}
	}

	@Test
	public void createStreamWithTags() {
		try {
			Set<String> tags = new HashSet<String>();
			tags.add("Concert");
			tags.add("Bikini");
			aStream = (Stream) glue.createStream("Concert Cypress Hill", null, true, true, tags, null, null, null,
					false, new Date().getTime(), 0, 10.255, 15.378, null);
		} catch (GlueException e) {
			fail("Exception during stream creation");
		}
	}

	@Test
	public void testJoinStream() {
		try {
			aStream = (Stream) glue.createStream("Concert Cypress Hill", null, true, true, null, null, null, null,
					false, new Date().getTime(), 0, 10.255, 15.378, null);
			glue.joinStream(aStream);
		} catch (GlueException e) {
			fail("Exception during stream creation");
		}
	}

	@Test
	public void testSearchStream() {
		try {
			List<IStream> result = glue.searchStreams("Test");
			System.out.println(result);
		} catch (GlueException e) {
			fail("Exception during stream creation");
		}
	}

}
