package com.glue.feed.img;

import java.util.Date;
import java.util.List;

import com.glue.domain.Event;
import com.glue.domain.Performer;
import com.glue.domain.Venue;

public interface ImageService {

    /**
     * Returns a list of all the events created (i.e. persisted) after the given
     * date and related to at least one representative image metadata (i.e. with
     * an image tagged sticky).
     * 
     * @param d
     *            a date
     * @return a list of events
     */
    List<Event> getEventsCreatedAfter(Date d);

    /**
     * Returns a list of all the venues created (i.e. persisted) after the given
     * date and related to at least one representative image metadata (i.e. with
     * an image tagged sticky).
     * 
     * @param d
     *            a date
     * @return a list of venues
     */
    List<Venue> getVenuesCreatedAfter(Date d);

    /**
     * Returns a list of all the performers created (i.e. persisted) after the
     * given date and related to at least one representative image metadata
     * (i.e. with an image tagged sticky).
     * 
     * @param d
     *            a date
     * @return a list of performers
     */
    List<Performer> getPerformersCreatedAfter(Date d);

}
