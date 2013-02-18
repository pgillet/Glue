package com.glue.webapp.auth;

import java.security.Principal;

import com.glue.struct.IUser;

public class UserPrincipal implements Principal, IUser {

	private final long id;
	private final String firstName;
	private final String lastName;
	private final String mailAddress;

	public UserPrincipal(IUser user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.mailAddress = user.getMailAddress();
	}

	@Override
	public String getName() {
		return this.mailAddress;
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		// immutable object
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
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
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
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
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (lastName == null) {
			if (other.lastName != null) {
				return false;
			}
		} else if (!lastName.equals(other.lastName)) {
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
