package com.glue.catalog.security;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Document
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Manager {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private @Id @GeneratedValue String id;

    @Indexed(unique = true)
    private String name;

    private @JsonIgnore String password;

    private String[] roles;

    public void setPassword(String password) {
	this.password = PASSWORD_ENCODER.encode(password);
    }

    protected Manager() {
    }

    public Manager(String name, String password, String... roles) {

	this.name = name;
	this.setPassword(password);
	this.roles = roles;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getPassword() {
	return password;
    }

    public String[] getRoles() {
	return roles;
    }

    public void setRoles(String[] roles) {
	this.roles = roles;
    }

    @Override
    public String toString() {
	return "Manager [id=" + id + ", name=" + name + ", roles="
		+ Arrays.toString(roles) + "]";
    }

}
