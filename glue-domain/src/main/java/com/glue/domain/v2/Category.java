package com.glue.domain.v2;

/**
 * The category object
 * 
 * 
 * 
 */

public class Category {

    /**
     * Category ID
     */
    private String id;

    /**
     * Category Name
     */
    private String name;

    /**
     * Return the category ID
     * 
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * Set the category ID
     * 
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * Get the category name
     * 
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Set the category name
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

}
