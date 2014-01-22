package com.glue.domain.impl;

import java.io.Serializable;

import com.glue.domain.IInvitedParticipant;

public class InvitedParticipant implements IInvitedParticipant, Serializable {

	private static final long serialVersionUID = -6228892883604245041L;

	private long id;

	private String name;

	private String mail;

	private long streamId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public long getStreamId() {
		return streamId;
	}

	public void setStreamId(long streamId) {
		this.streamId = streamId;
	}

}
