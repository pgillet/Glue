package com.glue.feed.nominatim;

import com.glue.domain.Venue;

public class NominatimVenueAdapter extends Venue {

    public NominatimVenueAdapter(NominatimVenue venue) {
	setName(venue.getName());
	setAddress(venue.getAddress().getFull());
	setCity(venue.getAddress().getCity());
	setLongitude(venue.getLon());
	setLatitude(venue.getLat());
    }

}
