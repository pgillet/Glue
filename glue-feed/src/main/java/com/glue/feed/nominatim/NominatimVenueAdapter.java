package com.glue.feed.nominatim;

import com.glue.domain.Venue;

public class NominatimVenueAdapter {

    public Venue build(NominatimVenue venue) {

	Venue v = new Venue();

	v.setName(venue.getName());
	v.setLongitude(venue.getLon());
	v.setLatitude(venue.getLat());
	v.setType(venue.getType());

	NominatimAddressDetail address = venue.getAddress();

	v.setAddress(address.getFull());
	v.setPostalCode(address.getPostcode());
	v.setCity(address.getCity());
	v.setRegion(address.getState());
	v.setCountry(address.getCountry());
	v.setCountryTwoLetterAbbreviation(address.getCountry_code());

	// address.getCounty();
	// address.getHouse_number();
	// address.getRoad();
	// address.getRetail();

	return v;
    }

}
