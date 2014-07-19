package com.glue.feed.dvr;

import java.util.Set;

import com.glue.domain.Event;
import com.glue.domain.Link;
import com.glue.domain.LinkType;

public class EventPriority implements Priority<Event> {

    private static int BOOKING_PRIORITY = 1000;

    /**
     * Priority per image.
     */
    private static int IMAGE_PRIORITY = 50;

    private static int DESCRIPTION_MAX_PRIORITY = 250;

    @Override
    public int getValue(Event event) {

	int val = 0;

	Set<Link> links = event.getLinks();
	for (Link link : links) {
	    if (LinkType.TICKET.equals(link.getType())) {
		val += BOOKING_PRIORITY;
		break;
	    }
	}

	val += event.getImages().size() * IMAGE_PRIORITY;

	if (event.getDescription() != null) {
	    int len = event.getDescription().length();
	    val += len > DESCRIPTION_MAX_PRIORITY ? DESCRIPTION_MAX_PRIORITY
		    : len;
	}

	return val;
    }

}
