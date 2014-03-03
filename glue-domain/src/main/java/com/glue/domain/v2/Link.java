package com.glue.domain.v2;

import java.util.Date;

/**
 * Implementation of the Link object.
 */
public class Link {

    /**
     * Link ID.
     */
    private int id;

    /**
     * Link URL.
     */
    private String url;

    /**
     * Type of link.
     */
    private LinkType type;

    private String typeString;

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
    public int getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
	this.id = id;
    }

    /**
     * @return the type
     */
    public int getType() {
	if (typeString != null) {
	    this.type = LinkType.valueOf(typeString.toUpperCase());
	}
	return type.asInteger();
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(LinkType type) {
	this.type = type;
	typeString = type.toString();
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

}
