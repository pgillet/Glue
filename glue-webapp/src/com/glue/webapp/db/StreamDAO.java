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

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_PUBLIC = "public";
	public static final String COLUMN_OPEN = "open";
	public static final String COLUMN_SECRET_QUESTION = "secret_question";
	public static final String COLUMN_SECRET_ANSWER = "secret_answer";
	public static final String COLUMN_REQUEST_TO_PARTICPATE = "request_to_participate";
	public static final String COLUMN_START_DATE = "start_date";
	public static final String COLUMN_END_DATE = "end_date";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_ADRESS = "address";

	public static final String INSERT_NEW_STREAM = "INSERT INTO stream(title, public, open, "
			+ "secret_question, secret_answer, request_to_participate, start_date, end_date, "
			+ "latitude, longitude, address) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_STREAM = "UPDATE stream SET title=?, public=?, open=?, "
			+ "secret_question=?, secret_answer=?, request_to_participate=?, start_date=?, end_date=?, "
			+ "latitude=?, longitude=?, address=? WHERE id=?";

	public static final String SELECT_STREAM = "SELECT * from stream WHERE id=?";

	public static final String DELETE_STREAM = "DELETE FROM stream WHERE id=?";

	Connection connection = null;
	PreparedStatement statement = null;

	public StreamDAO(Connection connection) {
		this.connection = connection;
	}

	public void create(StreamDTO myObject) throws SQLException {

		statement = connection.prepareStatement(INSERT_NEW_STREAM, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, myObject.getTitle());
		statement.setBoolean(2, myObject.isPublicc());
		statement.setBoolean(3, myObject.isOpen());
		statement.setString(4, myObject.getSharedSecretQuestion());
		statement.setString(5, myObject.getSharedSecretQuestion());
		statement.setBoolean(6, myObject.isShouldRequestToParticipate());
		statement.setLong(7, myObject.getStartDate());
		statement.setLong(8, myObject.getEndDate());
		statement.setDouble(9, myObject.getLatitude());
		statement.setDouble(10, myObject.getLongitude());
		statement.setString(11, myObject.getAddress());
		statement.executeUpdate();

		// Get the generated id
		ResultSet result = statement.getGeneratedKeys();
		Long id = null;
		while (result.next()) {
			id = result.getLong(1);
		}
		myObject.setId(id);
	}

	public void update(StreamDTO stream) throws SQLException {
		statement = connection.prepareStatement(UPDATE_STREAM, Statement.RETURN_GENERATED_KEYS);
		statement.setLong(12, stream.getId());
		statement.setString(1, stream.getTitle());
		statement.setBoolean(2, stream.isPublicc());
		statement.setBoolean(3, stream.isOpen());
		statement.setString(4, stream.getSharedSecretQuestion());
		statement.setString(5, stream.getSharedSecretAnswer());
		statement.setBoolean(6, stream.isShouldRequestToParticipate());
		statement.setLong(7, stream.getStartDate());
		statement.setLong(8, stream.getEndDate());
		statement.setDouble(9, stream.getLatitude());
		statement.setDouble(10, stream.getLongitude());
		statement.setString(11, stream.getAddress());
		statement.executeUpdate();
	}

	public void delete(long streamId) throws SQLException {
		statement = connection.prepareStatement(DELETE_STREAM, Statement.RETURN_GENERATED_KEYS);
		statement.setLong(1, streamId);
		statement.executeUpdate();
	}

	public StreamDTO search(long streamId) throws SQLException {
		StreamDTO result = null;
		statement = connection.prepareStatement(SELECT_STREAM, Statement.RETURN_GENERATED_KEYS);
		statement.setLong(1, streamId);
		ResultSet res = statement.executeQuery();
		if (res.next()) {
			result = new StreamDTO();
			result.setId(res.getLong(COLUMN_ID));
			result.setTitle(res.getString(COLUMN_TITLE));
			result.setPublicc(res.getBoolean(COLUMN_PUBLIC));
			result.setOpen(res.getBoolean(COLUMN_OPEN));
			result.setSharedSecretQuestion(res.getString(COLUMN_SECRET_QUESTION));
			result.setSharedSecretAnswer(res.getString(COLUMN_SECRET_ANSWER));
			result.setShouldRequestToParticipate(res.getBoolean(COLUMN_REQUEST_TO_PARTICPATE));
			result.setStartDate(res.getLong(COLUMN_START_DATE));
			result.setEndDate(res.getLong(COLUMN_END_DATE));
			result.setLatitude(res.getDouble(COLUMN_LATITUDE));
			result.setLongitude(res.getDouble(COLUMN_LONGITUDE));
			result.setAddress(res.getString(COLUMN_ADRESS));
		}
		return result;
	}
}
