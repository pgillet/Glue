package com.glue.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Tag object implementation.
 */
@Entity
public class Tag {

    /**
     * Tag ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * Tag title.
     */
    @Column(nullable = false, unique = true)
    private String title;

    /**
     * Tag owner.
     */
    private String owner;

    /**
     * Creates a tag.
     */
    public Tag() {
    }

    /**
     * Creates a tag.
     * 
     * @param title
     *            Title element
     */
    public Tag(String title) {
	this.title = title;
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
     * @return the owner
     */
    public String getOwner() {
	return owner;
    }

    /**
     * @param owner
     *            the owner to set
     */
    public void setOwner(String owner) {
	this.owner = owner;
    }

    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }
}
