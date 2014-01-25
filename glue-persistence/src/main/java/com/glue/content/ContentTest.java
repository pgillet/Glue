package com.glue.content;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.chemistry.opencmis.client.api.Folder;

import com.glue.domain.impl.Stream;
import com.glue.domain.impl.Venue;

public class ContentTest {

    public static void main(String[] args) throws IOException,
	    URISyntaxException {
	ContentManager cm = new ContentManager();
	VenueCAO venueCAO = cm.getVenueCAO();
	StreamCAO streamCAO = cm.getStreamCAO();

	Venue venue = new Venue();
	final long id = 12356;
	venue.setId(id);
	venue.setCity("Toulouse");

	Stream stream = new Stream();
	stream.setId(9987);
	stream.setStartDate(456789345676767L);
	stream.setVenue(venue);

	CmisPath path = streamCAO.getPath(stream);
	System.out.println("Stream path = " + path);

	Folder folder = streamCAO.getFolder(stream, true);
	System.out.println("Stream folder = " + folder);

	URL url = new URL(
		"http://cdn.funnie.st/wp-content/uploads/2013/11/539974_303885846368238_1455014827_n.jpg");

	streamCAO.add(url, stream);
    }

}
