package com.glue.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User object.
 */
@Entity
@Table(name = "Glue_User")
@XmlRootElement
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * Username.
     */
    private String username;

    /**
     * Password.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Mail address.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * User hometown.
     */
    private String hometown;

    /**
     * First Name.
     */
    private String firstName;

    /**
     * Last name.
     */
    private String lastName;

    /**
     * Interests.
     */
    private String interests;

    /**
     * Registration date.
     */
    private Date registrationDate;

    /**
     * Links about the user.
     */
    @OneToMany(cascade = { CascadeType.ALL })
    private Set<Link> links = new HashSet<>();

    /**
     * List of events the user is either watching or going;
     */
    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.PERSIST,
	    CascadeType.REFRESH, CascadeType.MERGE })
    private List<Event> going;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    /**
     * @return the email
     */
    public String getEmail() {
	return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
	this.email = email;
    }

    /**
     * First name
     * 
     * @return the firstName
     */
    public String getFirstName() {
	return firstName;
    }

    /**
     * First name
     * 
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     * List of events the user is going
     * 
     * @return the going
     */
    public List<Event> getGoing() {
	return going;
    }

    /**
     * List of events the user is going
     * 
     * @param going
     *            the going to set
     */
    public void setGoing(List<Event> going) {
	this.going = going;
    }

    /**
     * Users hometown
     * 
     * @return the hometown
     */
    public String getHometown() {
	return hometown;
    }

    /**
     * Users hometown
     * 
     * @param hometown
     *            the hometown to set
     */
    public void setHometown(String hometown) {
	this.hometown = hometown;
    }

    /**
     * User interests
     * 
     * @return the interests
     */
    public String getInterests() {
	return interests;
    }

    /**
     * Set user interests
     * 
     * @param interests
     *            the interests to set
     */
    public void setInterests(String interests) {
	this.interests = interests;
    }

    /**
     * User last name
     * 
     * @return the lastName
     */
    public String getLastName() {
	return lastName;
    }

    /**
     * User last name
     * 
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    /**
     * List of links
     * 
     * @return the links
     */
    public Set<Link> getLinks() {
	return links;
    }

    /**
     * Set the list of links
     * 
     * @param links
     *            the links to set
     */
    public void setLinks(Set<Link> links) {
	this.links = links;
    }

    /**
     * User registration date
     * 
     * @return the registrationDate
     */
    public Date getRegistrationDate() {
	return registrationDate;
    }

    /**
     * User registration date
     * 
     * @param registrationDate
     *            the registrationDate to set
     */
    public void setRegistrationDate(Date registrationDate) {
	this.registrationDate = registrationDate;
    }

    /**
     * User name
     * 
     * @return the username
     */
    public String getUsername() {
	return username;
    }

    /**
     * Set the username. Note that this field cannot be changed
     * 
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
	this.username = username;
    }

    /**
     * @return the password
     */
    // @XmlTransient
    public String getPassword() {
	return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
	this.password = password;
    }

}
