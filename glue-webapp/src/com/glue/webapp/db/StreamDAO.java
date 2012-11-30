package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.glue.struct.impl.dto.StreamDTO;

/**
 * DAO for Stream operations.
 * 
 * @author Greg
 * 
 */
public class StreamDAO {

	public static final String INSERT_NEW_STREAM = "INSERT INTO stream(title, public, open, "
			+ "secret_question, secret_answer, request_to_participate, start_date, end_date, "
			+ "latitude, longitude, address) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

	Connection connection = null;
	PreparedStatement statement = null;

	public StreamDAO(Connection connection) {
		this.connection = connection;
	}

	public void create(StreamDTO stream) throws SQLException {

		statement = connection.prepareStatement(INSERT_NEW_STREAM, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, stream.getTitle());
		statement.setBoolean(2, stream.isPublicc());
		statement.setBoolean(3, stream.isOpen());
		statement.setString(4, stream.getSharedSecretQuestion());
		statement.setString(5, stream.getSharedSecretQuestion());
		statement.setBoolean(6, stream.isShouldRequestToParticipate());
		statement.setLong(7, stream.getStartDate());
		statement.setLong(8, stream.getEndDate());
		statement.setDouble(9, stream.getLatitude());
		statement.setDouble(10, stream.getLongitude());
		statement.setString(11, stream.getAddress());
		statement.executeUpdate();

		// Get the generated id
		ResultSet result = statement.getGeneratedKeys();
		Long id = null;
		while (result.next()) {
			id = result.getLong(1);
		}
		stream.setId(id);
	}
}
