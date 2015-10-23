package com.glue.feed.nominatim;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.glue.domain.Venue;
import com.glue.feed.geo.GeoLocation;

public class NominatimRequester {

    public static final String BASE_URL = "http://open.mapquestapi.com/nominatim/v1/";
    public static final String SEARCH = "search.php";

    protected Client client = null;
    protected WebTarget target = null;

    public NominatimRequester() {
	ClientConfig clientConfig = new ClientConfig();
	clientConfig.register(JacksonFeature.class);
	clientConfig.property(ClientProperties.FOLLOW_REDIRECTS, true);

	client = ClientBuilder.newClient(clientConfig);
	target = client.target(BASE_URL);
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
     * @param boundingCoordinates
     *            Preferred area to find search results. May be null.
     * @param limit
     *            Limits the number of returned results to the integer entered.
     * @return a list of candidate venues. The list contains at most
     *         <i>limit</i> elements.
     */
    public synchronized List<Venue> search(String query,
	    GeoLocation[] boundingCoordinates, int limit) {

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

	WebTarget wt = target.path(SEARCH).queryParam("format", "json")
		.queryParam("key","Fmjtd|luur29612h,7w=o5-90rg0y")
		.queryParam("addressdetails", "1")
		.queryParam("limit", Integer.toString(limit))
		.queryParam("q", query);

	// Preferred area to find search results
	if (boundingCoordinates != null) {
	    // viewbox=left,top,right,bottom
	    StringBuilder sb = new StringBuilder()
		    .append(boundingCoordinates[0].getLongitudeInDegrees())
		    .append(",")
		    .append(boundingCoordinates[0].getLatitudeInDegrees())
		    .append(",")
		    .append(boundingCoordinates[1].getLongitudeInDegrees())
		    .append(",")
		    .append(boundingCoordinates[1].getLatitudeInDegrees());

	    wt.queryParam("viewbox", sb.toString());
	    // Do not restrict the search results to the bounding box
	    wt.queryParam("bounded", "0");
	}

	List<NominatimVenue> venues = wt.request().get(
		new GenericType<List<NominatimVenue>>() {
		});

	for (NominatimVenue nominatimVenue : venues) {
	    result.add(adapter.build(nominatimVenue));
	}

	return result;
    }

    /**
     * This search method considers the first result as the more relevant, and
     * calling this method is equivalent to calling
     * {@link #search(String, int, GeoLocation[])} with the limit argument equal
     * to 1.
     * 
     * @param query
     *            Query string being searched.
     * @param boundingCoordinates
     *            Preferred area to find search results. May be null.
     * @return the candidate venue, or null if none was found.
     */
    public Venue search(String query, GeoLocation[] boundingCoordinates) {
	List<Venue> venues = search(query, boundingCoordinates, 1);
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
	System.out.println(requester.search("le phare, toulouse", null));
    }
}
