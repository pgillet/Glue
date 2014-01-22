package com.glue.domain;

public interface IVenue {

	void setId(Long id);

	Long getId();

	void setUrl(String url);

	String getUrl();

	void setAddress(String address);

	String getAddress();

	void setLongitude(double longitude);

	double getLongitude();

	void setLatitude(double latitude);

	double getLatitude();

	void setName(String name);

	String getName();

	void setCity(String city);

	String getCity();
}
