package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.glue.struct.impl.dto.StreamDTO;

public class StreamDAO {

	public static final String INSERT_NEW_STREAM = "INSERT INTO stream(title, description, public, open, "
			+ "secretQuestion, secretAnswer, requestToParticipate, startDate, endDate, "
			+ "latitude, longitude, address) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	Connection connection = null;
	PreparedStatement statement = null;

	public StreamDAO() {
	}

	private Connection getConnection() throws SQLException {
		return DatabaseManager.getInstance().getConnection();
	}

	public void create(StreamDTO stream) {
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(INSERT_NEW_STREAM, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, stream.getTitle());
			statement.setString(2, stream.getDescription());
			statement.setBoolean(3, stream.isPublicc());
			statement.setBoolean(4, stream.isOpen());
			statement.setString(5, stream.getSharedSecretQuestion());
			statement.setString(6, stream.getSharedSecretQuestion());
			statement.setBoolean(7, stream.isShouldRequestToParticipate());
			statement.setLong(8, stream.getStartDate());
			statement.setLong(9, stream.getEndDate());
			statement.setDouble(10, stream.getLatitude());
			statement.setDouble(11, stream.getLongitude());
			statement.setString(12, stream.getAddress());
			statement.executeUpdate();

			// Get the id
			ResultSet result = statement.getGeneratedKeys();
			Long id = null;
			while (result.next()) {
				id = result.getLong(1);
			}
			stream.setId(id);
			connection.commit();
		} catch (SQLException e) {
			System.out.println(e);
			if (connection != null) {
				try {
					System.out.print("Transaction is being rolled back");
					connection.rollback();
				} catch (SQLException excep) {
					excep.printStackTrace();
				}
			}
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
