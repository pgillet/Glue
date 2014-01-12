package com.glue.feed.music.discogs;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscogsImages {

	@JsonProperty("uri")
	private String apiUrl;
	private int height;
	private int width;
	private String type;
	@JsonProperty("uri150")
	private String apiUrl150;

	public DiscogsImages() {
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getApiUrl150() {
		return apiUrl150;
	}

	public void setApiUrl150(String apiUrl150) {
		this.apiUrl150 = apiUrl150;
	}

	@Override
	public String toString() {
		return "DiscogsImages [apiUrl=" + apiUrl + ", height=" + height + ", width=" + width + ", type=" + type
				+ ", apiUrl150=" + apiUrl150 + "]";
	}

}
