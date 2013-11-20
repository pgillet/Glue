package com.glue.struct;

public interface IMedia {

	Long getId();

	void setId(Long id);

	IStream getStream();

	void setStream(IStream stream);

	IUser getUser();

	void setUser(IUser user);

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

	Long getCreationDate();

	void setCreationDate(Long date);

	boolean isExternal();

	void setExternal(boolean external);

	String getUrl();

	void setUrl(String url);

}
