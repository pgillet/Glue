package com.glue.webapp.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.glue.struct.impl.InvitedParticipant;

/**
 * DAO for User operations.
 * 
 * @author Greg
 * 
 */
public class InvitedParticipantDAO extends AbstractDAO {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_MAIL = "email";
	public static final String COLUMN_STREAM_ID = "stream_id";

	// INSERT IGNORE (see
	// http://www.tutorialspoint.com/mysql/mysql-handling-duplicates.htm)
	// No SQL exception if a duplicate has to be inserted.
	public static final String INSERT_NEW_IP = "INSERT IGNORE INTO INVITED(name, email, stream_id) VALUES (?,?,?)";
	public static final String SELECT_IP = "SELECT INVITED(id, name, email, stream_id) WHERE email=? and stream_id=?";
	public static final String DELETE_ALL_IP = "DELETE FROM INVITED WHERE stream_id=?";

	PreparedStatement statement = null;

	protected InvitedParticipantDAO() {
	}

	public void create(InvitedParticipant ip) throws SQLException {

		statement = connection.prepareStatement(INSERT_NEW_IP, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, ip.getName());
		statement.setString(2, ip.getMail());
		statement.setLong(3, ip.getStreamId());
		statement.executeUpdate();

		// Get the generated id
		ResultSet result = statement.getGeneratedKeys();
		Long id = null;
		while (result.next()) {
			id = result.getLong(1);
		}
		ip.setId(id);
	}

	public InvitedParticipant select(String mail, Long streamId) throws SQLException {

		InvitedParticipant ip = new InvitedParticipant();

		statement = connection.prepareStatement(SELECT_IP, Statement.RETURN_GENERATED_KEYS);

		// Get the first result
		ResultSet result = statement.getGeneratedKeys();
		while (result.next()) {
			ip.setId(result.getLong(COLUMN_ID));
			ip.setName(result.getString(COLUMN_NAME));
			ip.setMail(result.getString(COLUMN_MAIL));
			ip.setStreamId(result.getLong(COLUMN_STREAM_ID));
		}
		return ip;
	}

	public void deleteAll(long streamId) throws SQLException {
		statement = connection.prepareStatement(DELETE_ALL_IP);
		statement.setLong(1, streamId);
		statement.executeUpdate();
	}
}
