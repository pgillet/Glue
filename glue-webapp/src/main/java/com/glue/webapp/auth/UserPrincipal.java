package com.glue.webapp.auth;

import java.security.Principal;

import com.glue.struct.IUser;

public class UserPrincipal implements Principal, IUser {

	private final long id;
	private final String name;
	private final String mailAddress;

	public UserPrincipal(IUser user) {
		this.id = user.getId();
		this.name = user.getName();
		this.mailAddress = user.getMailAddress();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.mailAddress;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		// immutable object
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String firstName) {
		// immutable object
	}

	/**
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}

	/**
	 * @param mailAddress
	 *            the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		// immutable object
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return null;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		// immutable object
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((mailAddress == null) ? 0 : mailAddress.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UserPrincipal other = (UserPrincipal) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (mailAddress == null) {
			if (other.mailAddress != null) {
				return false;
			}
		} else if (!mailAddress.equals(other.mailAddress)) {
			return false;
		}
		return true;
	}

}
