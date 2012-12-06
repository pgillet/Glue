package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

import com.glue.struct.IStream;
import com.glue.struct.impl.InvitedParticipant;
import com.glue.struct.impl.Stream;

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

	public void create(IStream aStream) throws SQLException {

		statement = connection.prepareStatement(INSERT_NEW_STREAM, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, aStream.getTitle());
		statement.setBoolean(2, aStream.isPublicc());
		statement.setBoolean(3, aStream.isOpen());
		statement.setString(4, aStream.getSharedSecretQuestion());
		statement.setString(5, aStream.getSharedSecretQuestion());
		statement.setBoolean(6, aStream.isShouldRequestToParticipate());
		statement.setLong(7, aStream.getStartDate());
		statement.setLong(8, aStream.getEndDate());
		statement.setDouble(9, aStream.getLatitude());
		statement.setDouble(10, aStream.getLongitude());
		statement.setString(11, aStream.getAddress());
		statement.executeUpdate();

		// Get the generated id
		ResultSet result = statement.getGeneratedKeys();
		Long id = null;
		while (result.next()) {
			id = result.getLong(1);
		}
		aStream.setId(id);

		// Invited participant
		updateInvitedParticipant(aStream);

		// Tags
		updateTags(aStream);

		// TODO set as administrator
	}

	public void update(IStream aStream) throws SQLException {
		statement = connection.prepareStatement(UPDATE_STREAM, Statement.RETURN_GENERATED_KEYS);
		statement.setLong(12, aStream.getId());
		statement.setString(1, aStream.getTitle());
		statement.setBoolean(2, aStream.isPublicc());
		statement.setBoolean(3, aStream.isOpen());
		statement.setString(4, aStream.getSharedSecretQuestion());
		statement.setString(5, aStream.getSharedSecretAnswer());
		statement.setBoolean(6, aStream.isShouldRequestToParticipate());
		statement.setLong(7, aStream.getStartDate());
		statement.setLong(8, aStream.getEndDate());
		statement.setDouble(9, aStream.getLatitude());
		statement.setDouble(10, aStream.getLongitude());
		statement.setString(11, aStream.getAddress());
		statement.executeUpdate();

		// Invited participant
		updateInvitedParticipant(aStream);

		// Store tags
		updateTags(aStream);
	}

	public void delete(long streamId) throws SQLException {
		statement = connection.prepareStatement(DELETE_STREAM, Statement.RETURN_GENERATED_KEYS);
		statement.setLong(1, streamId);
		statement.executeUpdate();
	}

	public IStream search(long streamId) throws SQLException {
		Stream result = null;
		statement = connection.prepareStatement(SELECT_STREAM, Statement.RETURN_GENERATED_KEYS);
		statement.setLong(1, streamId);
		ResultSet res = statement.executeQuery();
		if (res.next()) {
			result = new Stream();
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

	private void updateInvitedParticipant(IStream aStream) throws SQLException {
		// Store invited participants
		InvitedParticipantDAO ipDAO = new InvitedParticipantDAO(connection);

		// Delete previous invited participant ...
		ipDAO.deleteAll(aStream.getId());

		// Create all invited participant
		Map<String, String> ipList = aStream.getInvitedParticipants();
		if (ipList != null) {
			InvitedParticipant ip = new InvitedParticipant();
			for (Map.Entry<String, String> entry : ipList.entrySet()) {
				ip.setMail(entry.getKey());
				ip.setName(entry.getValue());
				ip.setStreamId(aStream.getId());
				ipDAO.create(ip);
			}
		}
	}

	private void updateTags(IStream aStream) throws SQLException {
		// Store invited participants
		TagDAO tagDAO = new TagDAO(connection);

		// Delete previous invited participant ...
		tagDAO.deleteAll(aStream.getId());

		// Create all invited participant
		Set<String> tagSet = aStream.getTags();
		if (tagSet != null) {
			for (String aTag : tagSet) {
				tagDAO.create(aTag, aStream.getId());
			}
		}

	}
}
