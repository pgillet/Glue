package com.glue.feed.nominatim;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class NominatimAddressDetail {

	private String house_number;
	private String road;
	private String retail;
	private String city;
	private String county;
	private String state;
	private String postcode;
	private String country;
	private String country_code;
	private String continent;
	
	public NominatimAddressDetail() {
		// TODO Auto-generated constructor stub
	}

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	public String getRetail() {
		return retail;
	}

	public void setRetail(String retail) {
		this.retail = retail;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getHouse_number() {
		return house_number;
	}

	public void setHouse_number(String house_number) {
		this.house_number = house_number;
	}
	
	public String getFull() {
		// Retrieve address
		StringBuilder result = new StringBuilder("");

		if (!"".equals(StringUtils.defaultString(house_number))) {
			result.append(house_number).append(", ");
		}

		if (!"".equals(StringUtils.defaultString(road))) {
			result.append(road).append(", ");
		}

		if (!"".equals(StringUtils.defaultString(retail))) {
			result.append(retail).append(", ");
		}

		if (!"".equals(StringUtils.defaultString(postcode))) {
			result.append(postcode).append(" ");
		}

		if (!"".equals(StringUtils.defaultString(city))) {
			result.append(city);
		}

		return result.toString().trim();
	}

}
