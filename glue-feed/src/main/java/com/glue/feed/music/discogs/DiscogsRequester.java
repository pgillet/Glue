package com.glue.feed.music.discogs;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;

public class DiscogsRequester {

    public static final String BASE_URL = "http://api.discogs.com/";
    public static final String SEARCH = "database/search";

    protected Client client = null;
    protected WebTarget target = null;

    public DiscogsRequester() {
	ClientConfig clientConfig = new ClientConfig();
	clientConfig.register(JacksonFeature.class);
	clientConfig.property(ClientProperties.FOLLOW_REDIRECTS, true);

	client = ClientBuilder.newClient(clientConfig);
	target = client.target(BASE_URL);
    }

    public List<DiscogsPerfomer> getPerformers(String query)
	    throws InterruptedException {

	List<DiscogsPerfomer> performers = new ArrayList<>();

	// First looking for all perfomer summary
	DiscogsSearchResult searchResult = target.path(SEARCH)
		.queryParam("type", "artist").queryParam("q", "title:" + query)
		.request().get(DiscogsSearchResult.class);

	// Ok, there is some result
	DiscogsPerfomer performer;
	if (searchResult.getPerformersSummary() != null) {

	    for (DiscogsPerformerSummary summary : searchResult
		    .getPerformersSummary()) {

		// !!! Waiting for 1 second !!! DO NOT DELETE !!!
		Thread.sleep(1000);
		performer = target.path(
			StringUtils.substringAfter(summary.getApiUrl(),
				BASE_URL)).request().get(DiscogsPerfomer.class);
		performer.setSummary(summary);
		performers.add(performer);
	    }
	}
	return performers;
    }

    /**
     * Test.
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
	DiscogsRequester requester = new DiscogsRequester();
	List<DiscogsPerfomer> perfomers = requester.getPerformers("june of 44");
	System.out.println(perfomers);
    }

}
