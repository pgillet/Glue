package com.glue.struct;

public interface IMedia {

	Long getId();

	void setId(Long id);

	Long getStreamId();

	void setStreamId(Long streamId);

	String getExtension();

	void setExtension(String extension);

	String getMimeType();

	void setMimeType(String mimeType);

	String getCaption();

	void setCaption(String caption);

	Double getLatitude();

	void setLatitude(Double latitude);

	Double getLongitude();

	void setLongitude(Double longitude);

	Long getStartDate();

	void setStartDate(Long date);

}
