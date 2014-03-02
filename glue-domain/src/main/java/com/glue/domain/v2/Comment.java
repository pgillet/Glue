package com.glue.domain.v2;

import java.util.Date;

/**
 * Comment Object
 * 
 * 
 * 
 */

public class Comment {

    /**
     * Comment ID
     */

    private String id;

    /**
     * Comment text
     */
    private String text;

    /**
     * Username who generated the comment
     */
    private String username;

    private Date time;

    /**
     * Create a new comment
     */
    public Comment() {
    }

    /**
     * Create a comment with the given text
     * 
     * @param text
     */
    public Comment(String text) {
	this.text = text;
    }

    /**
     * Return comment ID
     * 
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * Set comment ID
     * 
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * Return comment text
     * 
     * @return the text
     */
    public String getText() {
	return text;
    }

    /**
     * Set comment text
     * 
     * @param text
     *            the text to set
     */
    public void setText(String text) {
	this.text = text;
    }

    /**
     * Get the time the comment was made
     * 
     * @return the time
     */
    public Date getTime() {
	return time;
    }

    /**
     * Set the comment time
     * 
     * @param time
     *            the time to set
     */
    public void setTime(Date time) {
	this.time = time;
    }

    /**
     * Username that made the comment
     * 
     * @return the username
     */
    public String getUsername() {
	return username;
    }

    /**
     * Set the name of the user making the comment
     * 
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
	this.username = username;
    }

}
