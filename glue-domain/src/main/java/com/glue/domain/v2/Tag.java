package com.glue.domain.v2;

/**
 * Tag object implementation
 * 
 * 
 * 
 */

public class Tag {

    /**
     * Tag ID
     */

    private String id;

    /**
     * Tag title
     */
    private String title;

    /**
     * Tag owner
     */
    private String owner;

    /**
     * Creates a tag
     */
    public Tag() {
    }

    /**
     * Creates a tag
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
