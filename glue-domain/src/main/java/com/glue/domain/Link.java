package com.glue.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Implementation of the Link object.
 */
@Entity
public class Link {

    /**
     * Link ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * Link URL.
     */
    @Column(nullable = false, length = 2048)
    private String url;

    /**
     * Type of link.
     */
    @Enumerated(EnumType.STRING)
    private LinkType type;

    /**
     * Link description.
     */
    private String description;

    /**
     * User who created the link.
     */
    private String username;

    /**
     * Time the link was created.
     */
    private Date created;

    /**
     * @return the created
     */
    public Date getCreated() {
	return created;
    }

    /**
     * @param created
     *            the created to set
     */
    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * @return the type
     */
    public LinkType getType() {
	return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(LinkType type) {
	this.type = type;
    }

    /**
     * @return the url
     */
    public String getUrl() {
	return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
	this.url = url;
    }

    /**
     * @return the username
     */
    public String getUsername() {
	return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
	this.username = username;
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
	result = prime * result + ((url == null) ? 0 : url.hashCode());
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
	Link other = (Link) obj;
	if (url == null) {
	    if (other.url != null) {
		return false;
	    }
	} else if (!url.equals(other.url)) {
	    return false;
	}
	return true;
    }

}
