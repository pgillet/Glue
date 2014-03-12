package com.glue.feed.nominatim;

import java.util.ArrayList;
import java.util.List;

import com.glue.domain.Venue;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class NominatimRequester {

	public static final String BASE_URL = "http://open.mapquestapi.com/nominatim/v1/";
	public static final String SEARCH = "search.php";

	protected Client client = null;
	protected WebResource ressource = null;

	public NominatimRequester() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		clientConfig.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		client = Client.create(clientConfig);
		ressource = client.resource(BASE_URL);
	}

	public List<Venue> getVenues(String query) {

		List<Venue> result = new ArrayList<>();

		List<NominatimVenue> venues = ressource
				.path(SEARCH)
				.queryParam("format", "json")
				.queryParam("addressdetails", "1")
				.queryParam("limit", "20")
				.queryParam("q", query)
				.get(new GenericType<List<NominatimVenue>>() {});

		for (NominatimVenue nominatimVenue : venues) {
			result.add(new NominatimVenueAdapter(nominatimVenue));
		}

		return result;
	}

	/**
	 * Test.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		NominatimRequester requester = new NominatimRequester();
		System.out.println(requester.getVenues("le phare, toulouse"));
	}
}
