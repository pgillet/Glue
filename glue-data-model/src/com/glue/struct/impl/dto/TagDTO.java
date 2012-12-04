package com.glue.struct.impl.dto;

import java.io.Serializable;

import com.glue.struct.ITag;

public class TagDTO implements ITag, Serializable {

	private static final long serialVersionUID = 2178322657611238211L;

	private long id;

	private String tag;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
