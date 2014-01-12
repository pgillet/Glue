package com.glue.feed.music.discogs;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

// result sample
//{"pagination": {"per_page": 50, "items": 3, "page": 1, "urls": {}, "pages": 1}, "results": [{"thumb": "http://api.discogs.com/image/A-90-17199-1387966325-8893.jpeg", "title": "Sonic Youth", "uri": "/artist/Sonic+Youth", "resource_url": "http://api.discogs.com/artists/17199", "type": "artist", "id": 17199}

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscogsSearchResult {
	
	@JsonProperty("results")
	List<DiscogsPerformerSummary>  performersSummary;

	public DiscogsSearchResult() {
	}
	
	public List<DiscogsPerformerSummary> getPerformersSummary() {
		return performersSummary;
	}

	public void setPerformersSummary(List<DiscogsPerformerSummary> performersSummary) {
		this.performersSummary = performersSummary;
	}
	
}
