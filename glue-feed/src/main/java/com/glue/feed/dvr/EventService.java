package com.glue.feed.dvr;

import java.util.Date;
import java.util.List;

import com.glue.domain.Event;

public interface EventService extends AutoCloseable {

    /**
     * Find events created after the the given date, which are not withdrawn and
     * whose the venue has a parent reference venue or is a reference venue
     * itself.
     * 
     * @return a list of events
     */
    List<Event> getUnresolvedEvents(Date limit, int start, int max);

    /**
     * Find events which are neither the given event, nor withdrawn, with the
     * same reference venue and similar dates but whose the data source is
     * different.
     * 
     * @return a list of events
     */
    List<Event> getPotentialDuplicates(Event e);

    /**
     * TODO: To be defined.
     * 
     * @param e1
     * @param e2
     */
    void resolve(Event e1, Event e2);

}
