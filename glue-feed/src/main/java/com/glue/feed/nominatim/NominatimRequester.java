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
	clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
		Boolean.TRUE);
	clientConfig.getProperties().put(
		ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

	client = Client.create(clientConfig);
	ressource = client.resource(BASE_URL);
    }

    /**
     * Search terms are processed left to right and house numbers, where
     * defined, will be used. Commas are optional but will improve performance
     * by reducing the complexity of the search.
     * 
     * Enclosing the query string in [] causes Nominatim to do a facility
     * search. For example, q=[pub] returns results where type=pub in
     * OpenStreetMap.
     * 
     * @param query
     *            Query string being searched.
     * @param limit
     *            Limits the number of returned results to the integer entered.
     * @return a list of candidate venues. The list contains at most
     *         <i>limit</i> elements.
     */
    public synchronized List<Venue> search(String query, int limit) {

	final long timeout = 1000;
	List<Venue> result = new ArrayList<>();
	NominatimVenueAdapter adapter = new NominatimVenueAdapter();

	// Max. 1 request/s to not overload the Nominatim search service
	try {
	    Thread.sleep(timeout);
	} catch (InterruptedException e) {
	    // Since this application has not defined another thread to cause
	    // the interrupt, it doesn't bother to catch InterruptedException.
	}

	List<NominatimVenue> venues = ressource.path(SEARCH)
		.queryParam("format", "json").queryParam("addressdetails", "1")
		.queryParam("limit", Integer.toString(limit))
		.queryParam("q", query)
		.get(new GenericType<List<NominatimVenue>>() {
		});

	for (NominatimVenue nominatimVenue : venues) {
	    result.add(adapter.build(nominatimVenue));
	}

	return result;
    }

    /**
     * This search method considers the first result as the more relevant, and
     * calling this method is equivalent to calling {@link #search(String, int)}
     * with the limit argument equal to 1.
     * 
     * @param query
     *            Query string being searched.
     * @return the candidate venue, or null if none was found.
     */
    public Venue search(String query) {
	List<Venue> venues = search(query, 1);
	if (!venues.isEmpty()) {
	    return venues.get(0);
	}

	return null;
    }

    /**
     * Test.
     * 
     * @param args
     */
    public static void main(String[] args) {
	NominatimRequester requester = new NominatimRequester();
	System.out.println(requester.search("le phare, toulouse"));
    }
}
