package com.glue.feed.music.discogs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class DiscogsRequester {

	public static final String BASE_URL = "http://api.discogs.com/";
	public static final String SEARCH = "database/search";

	protected Client client = null;
	protected WebResource ressource = null;

	public DiscogsRequester() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		clientConfig.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		client = Client.create(clientConfig);
		ressource = client.resource(BASE_URL);
	}

	public List<DiscogsPerfomer> getPerformers(String query) throws InterruptedException {

		
		List<DiscogsPerfomer> performers = new ArrayList<>();
		
		// First looking for all perfomer summary
		DiscogsSearchResult searchResult = ressource
				.path(SEARCH)
				.queryParam("type", "artist")
				.queryParam("q", "title:" + query)
				.get(DiscogsSearchResult.class);
		
		// Ok, there is some result
		DiscogsPerfomer performer;
		if (searchResult.getPerformersSummary()!=null) {
			
			for (DiscogsPerformerSummary summary : searchResult.getPerformersSummary()) {
				
				// !!! Waiting for 1 second !!! DO NOT DELETE !!!
				Thread.sleep(1000);
				performer = ressource
						.path(StringUtils.substringAfter(summary.getApiUrl(),BASE_URL))
						.get(DiscogsPerfomer.class);
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
