package com.glue.api;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.domain.Event;
import com.glue.domain.Link;
import com.glue.exceptions.GlueException;

public class MediaTest {

    Glue glue;
    Link media;

    @Before
    public void setUp() throws Exception {
	glue = new GlueFactory().getInstance();
    }

    @Test
    public void testCreateMedia1() {
	try {

	    InputStream is = new FileInputStream("C:\\Glue_Stream\\Nutella.jpg");

	    // Create a stream
	    Event event = glue.createEvent("Concert", null, true, true, null,
		    null, null, null, false, new Date(), null, 10.255, 15.378,
		    null);

	    // media = glue.createMedia(stream.getId(), "caption", "jpg",
	    // "Picture", 10.255, 15.378, new Date().getTime(),
	    // is);
	} catch (GlueException e) {
	    fail("Exception during stream creation");
	} catch (FileNotFoundException e) {
	    fail("Exception during stream creation");
	}
    }
}
