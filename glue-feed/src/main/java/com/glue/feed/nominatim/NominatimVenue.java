package com.glue.feed.nominatim;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NominatimVenue {

	private String osm_type;
	private double lat;
	private double lon;
	private String display_name;
	@JsonProperty("class")
	private String clazz;
	private String type;
	private double importance;
	private NominatimAddressDetail address;

	// Empty constructor
	public NominatimVenue() {
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getOsm_type() {
		return osm_type;
	}

	public void setOsm_type(String osm_type) {
		this.osm_type = osm_type;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getImportance() {
		return importance;
	}

	public void setImportance(double importance) {
		this.importance = importance;
	}

	public NominatimAddressDetail getAddress() {
		return address;
	}

	public void setAddress(NominatimAddressDetail address) {
		this.address = address;
	}
	
	public String getName() {
		return StringUtils.substringBefore(display_name, ",");
	}

}
