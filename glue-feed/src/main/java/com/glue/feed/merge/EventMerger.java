package com.glue.feed.merge;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.glue.domain.Event;
import com.glue.domain.Image;
import com.glue.feed.dvr.EventPriority;

public class EventMerger implements Merger<Event> {

    @Override
    public Event merge(Event e1, Event e2) {

	Event withdrawnEvent = e1;

	// Same source : Keep the fresher event
	if (StringUtils.equals(e1.getSource(), e2.getSource())) {

	    if (e1.getCreated().after(e2.getCreated())) {

		// e2 must be withdrawn
		withdrawnEvent = e2;
	    }
	}

	// Different Sources, compute priority
	else {
	    // Compute absolute event priorities
	    EventPriority p = new EventPriority();

	    int p1 = p.getValue(e1);
	    int p2 = p.getValue(e2);

	    if (p1 > p2) {
		withdrawnEvent = e2;
	    }
	}

	// Then merge images
	Event eventToKeep = (withdrawnEvent == e1 ? e2 : e1);
	mergeImages(withdrawnEvent, eventToKeep);
	withdrawnEvent.setWithdrawn(true);
	withdrawnEvent.setWithdrawnNote("Duplicate of " + eventToKeep.getId());

	return withdrawnEvent;
    }

    /**
     * Get images from withdrawn event if eventToKeep has no images yet.
     * 
     * @param withdrawnEvent
     * @param eventToKeep
     */
    private void mergeImages(Event withdrawnEvent, Event eventToKeep) {

	List<Image> withdrawnImages = withdrawnEvent.getImages();
	List<Image> eventImages = withdrawnEvent.getImages();

	if (eventImages != null && withdrawnImages != null
		&& eventImages.isEmpty() && !withdrawnImages.isEmpty()) {
	    eventToKeep.setImages(withdrawnImages);
	}
    }

}
