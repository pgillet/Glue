package com.glue.webapp.auth;

import java.security.Principal;

import com.glue.domain.User;

public class UserPrincipal implements Principal {

    private final String id;
    private final String name;
    private final String mailAddress;

    public UserPrincipal(User user) {
	this.id = user.getId();
	this.name = user.getUsername();
	this.mailAddress = user.getEmail();
    }

    @Override
    public String getName() {
	return this.name;
    }

    @Override
    public String toString() {
	return this.mailAddress;
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
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result
		+ ((mailAddress == null) ? 0 : mailAddress.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
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
	if (id == null) {
	    if (other.id != null) {
		return false;
	    }
	} else if (!id.equals(other.id)) {
	    return false;
	}
	if (mailAddress == null) {
	    if (other.mailAddress != null) {
		return false;
	    }
	} else if (!mailAddress.equals(other.mailAddress)) {
	    return false;
	}
	if (name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!name.equals(other.name)) {
	    return false;
	}
	return true;
    }

}
