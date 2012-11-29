package com.glue.webapp.db;

public class DBUtilities {

	public static final String INSERT_NEW_STREAM = "INSERT INTO stream(title, description, public, open, "
			+ "secretQuestion, secretAnswer, requestToParticipate, startDate, endDate, "
			+ "latitude, longitude, address) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
}
