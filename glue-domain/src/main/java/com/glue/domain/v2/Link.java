package com.glue.domain.v2;

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

}
