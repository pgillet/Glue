package com.glue.feed.music.discogs;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscogsPerformerSummary {

	@JsonProperty("title")
	private String name;
	private String thumb;
	@JsonProperty("resource_url")
	private String apiUrl;
	
	public DiscogsPerformerSummary() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String url) {
		this.apiUrl = url;
	}

	@Override
	public String toString() {
		return "DiscogsPerformerSummary [name=" + name + ", thumb=" + thumb + ", apiUrl=" + apiUrl + "]";
	}

}
