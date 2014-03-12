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
import com.glue.domain.Event;
import com.glue.exceptions.GlueException;

public class StreamTest {

    Glue glue;
    Event aEvent;

    @Before
    public void setUp() throws Exception {
	glue = new GlueFactory().getInstance();
    }

    @Test
    public void createAnEmptyStream() {
	try {
	    aEvent = glue.createEvent("Concert Nirvana",
		    "Concert au bikini, lundi 9 decembre", true, true, null,
		    null, null, null, false, new Date(), new Date(), 10.255,
		    15.378, null);
	} catch (GlueException e) {
	    fail("Exception during stream creation");
	}
    }

    @Test
    public void createAndUpdate() {
	try {
	    aEvent = (Event) glue.createEvent("Stream", null, true, true, null,
		    null, null, null, false, new Date(), new Date(), 10.255,
		    15.378, null);
	    ((Event) aEvent).setDescription("Update desc");
	    aEvent = (Event) glue.updateEvent(aEvent);
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
	    aEvent = (Event) glue.createEvent("Concert Cypress Hill", null,
		    true, true, null, ipList, null, null, false, new Date(),
		    null, 10.255, 15.378, null);
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
	    aEvent = (Event) glue.createEvent("Concert Cypress Hill", null,
		    true, true, tags, null, null, null, false, new Date(),
		    null, 10.255, 15.378, null);
	} catch (GlueException e) {
	    fail("Exception during stream creation");
	}
    }

    @Test
    public void testJoinStream() {
	try {
	    aEvent = (Event) glue.createEvent("Concert Cypress Hill", null,
		    true, true, null, null, null, null, false, new Date(),
		    null, 10.255, 15.378, null);
	    glue.joinEvent(aEvent);
	} catch (GlueException e) {
	    fail("Exception during stream creation");
	}
    }

    @Test
    public void testSearchStream() {
	try {
	    List<Event> result = glue.searchEvents("Test");
	    System.out.println(result);
	} catch (GlueException e) {
	    fail("Exception during stream creation");
	}
    }

}
