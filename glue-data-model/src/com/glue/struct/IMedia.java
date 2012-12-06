package com.glue.struct;

public interface IMedia {

	long getId();

	void setId(long id);

	long getStreamId();

	void setStreamId(long streamId);

	String getExtension();

	void setExtension(String extension);

	String getMimeType();

	void setMimeType(String mimeType);

	String getCaption();

	void setCaption(String caption);

	double getLatitude();

	void setLatitude(double latitude);

	double getLongitude();

	void setLongitude(double longitude);

	long getStartDate();

	void setStartDate(long date);

}
