package com.glue.feed.dvr;

import java.util.List;

import com.glue.domain.Venue;

public interface VenueService extends AutoCloseable {

    /**
     * Find venues that are not reference venues and have no parent.
     * 
     * @return a list of venues
     */
    List<Venue> getUnresolvedVenues(int limit);

    /**
     * Returns the reference venue for the given venue, or null if none was
     * found.
     * 
     * @param venue
     * @return
     */
    Venue resolve(Venue venue);

}
