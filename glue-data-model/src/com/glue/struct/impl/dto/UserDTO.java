package com.glue.struct.impl.dto;

import java.io.Serializable;

import com.glue.struct.IUser;

public class UserDTO implements IUser, Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String firstName;

	private String lastName;

	private String mail;

	private String password;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
