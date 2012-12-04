package com.glue.struct.impl.dto;

import java.io.Serializable;

public class StreamTagDTO implements Serializable {

	private static final long serialVersionUID = 5090381992590558317L;

	private long id;

	private long tagid;

	private long streamId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTagId() {
		return tagid;
	}

	public void setTagId(long tagid) {
		this.tagid = tagid;
	}

	public long getStreamId() {
		return streamId;
	}

	public void setStreamId(long streamId) {
		this.streamId = streamId;
	}

}
