package com.glue.struct.impl;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.glue.struct.IUser;

@XmlRootElement
public class User implements IUser, Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private String mailAddress;

	private String password;

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

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	//@XmlTransient
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
