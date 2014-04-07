package com.glue.content;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.commons.io.FilenameUtils;

import com.glue.domain.Event;
import com.glue.domain.Venue;

public class ContentTest {

    public static void main(String[] args) throws IOException,
	    URISyntaxException {
	ContentManager cm = new ContentManager();

	System.out.println("Repository info = "
		+ cm.getSession().getRepositoryInfo());

	VenueCAO venueCAO = cm.getVenueCAO();
	EventCAO eventCAO = cm.getEventCAO();

	Venue venue = new Venue();
	final String id = "12356";
	venue.setId(id);
	venue.setCity("Toulouse");

	Event event = new Event();
	event.setId("9987");
	event.setStartTime(Calendar.getInstance().getTime());
	event.setVenue(venue);

	CmisPath path = eventCAO.getPath(event);

	System.out.println("Stream path = " + path);

	Folder folder = eventCAO.getFolder(event, true);
	System.out.println("Stream folder = " + folder);

	String url = "http://cdn.funnie.st/wp-content/uploads/2013/11/539974_303885846368238_1455014827_n.jpg";
	String basename = FilenameUtils.getBaseName(url);

	eventCAO.addImage(url, event);

	String other = eventCAO.getImageURL(basename, event,
		ImageRendition.THUMBNAIL);
	
	System.out.println("Document URL = " + other);	

    }

}
