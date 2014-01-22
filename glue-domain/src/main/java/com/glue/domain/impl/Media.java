package com.glue.domain.impl;

import java.io.Serializable;

import com.glue.domain.IMedia;
import com.glue.domain.IStream;
import com.glue.domain.IUser;

public class Media implements IMedia, Serializable {

	private static final long serialVersionUID = 8398408650348533491L;

	Long id;
	String caption;
	String extension;
	String mimeType;
	Double latitude;
	Double longitude;
	Long creationDate;
	IStream stream;
	IUser user;
	boolean external;
	String url;

	public Media() {
	}

	public IStream getStream() {
		return stream;
	}

	public void setStream(IStream stream) {
		this.stream = stream;
	}

	public IUser getUser() {
		return user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public boolean isExternal() {
		return external;
	}

	@Override
	public void setExternal(boolean external) {
		this.external = external;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Media [id=" + id + ", caption=" + caption + ", extension=" + extension + ", mimeType=" + mimeType
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", creationDate=" + creationDate
				+ ", streamId=" + stream.getId() + "]";
	}

}
