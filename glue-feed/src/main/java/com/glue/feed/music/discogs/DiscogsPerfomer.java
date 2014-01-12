package com.glue.feed.music.discogs;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscogsPerfomer {
	
	private DiscogsPerformerSummary summary;
	
	private String name;
	
	//Discogs URL
	@JsonProperty("uri")
	private String discogsUrl;
	@JsonProperty("urls")
	private List<String> externalLinks;
	private List<DiscogsImages> images;
	
	// Other names
	@JsonProperty("namevariations")
	private List<String> otherNames;
	
	public DiscogsPerfomer() {
		// TODO Auto-generated constructor stub
	}
	
	public void setSummary(DiscogsPerformerSummary summary) {
		this.summary = summary;
	}
	
	public String getThumbApiPath() {
		return summary.getThumb();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiscogsUrl() {
		return discogsUrl;
	}

	public void setDiscogsUrl(String discogsUrl) {
		this.discogsUrl = discogsUrl;
	}

	public List<String> getExternalLinks() {
		return externalLinks;
	}

	public void setExternalLinks(List<String> externalLinks) {
		this.externalLinks = externalLinks;
	}

	public List<DiscogsImages> getImages() {
		return images;
	}

	public void setImages(List<DiscogsImages> images) {
		this.images = images;
	}

	public List<String> getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(List<String> otherNames) {
		this.otherNames = otherNames;
	}

	@Override
	public String toString() {
		return "DiscogsPerfomer [name=" + name + ", discogsUrl=" + discogsUrl + ", externalLinks="
				+ externalLinks + ", images=" + images + ", otherNames=" + otherNames + ", thumb="
				+ getThumbApiPath() + "]";
	}
}
