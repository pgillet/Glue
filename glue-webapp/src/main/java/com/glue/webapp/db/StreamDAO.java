package com.glue.webapp.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
public class StreamDAO extends AbstractDAO {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_STREAM_ID = "stream_id";
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
	public static final String COLUMN_NB_OF_PARTICIPANT = "nb_of_participant";
	public static final String COLUMN_THUMB_PATH = "thumb_path";

	public static final String INSERT_NEW_STREAM = "INSERT INTO stream(title, public, open, "
			+ "secret_question, secret_answer, request_to_participate, start_date, end_date, "
			+ "latitude, longitude, address, thumb_path) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_NEW_PARTICPANT = "INSERT IGNORE INTO PARTICIPANT(user_id, stream_id, admin) VALUES (?,?,?)";

	public static final String UPDATE_STREAM = "UPDATE stream SET title=?, public=?, open=?, "
			+ "secret_question=?, secret_answer=?, request_to_participate=?, start_date=?, end_date=?, "
			+ "latitude=?, longitude=?, address=? WHERE id=?";

	public static final String SELECT_STREAM = "SELECT * from stream WHERE id=?";

	public static final String SELECT_STREAM_VIEW = "SELECT * from STREAM_VIEW";

	public static final String SELECT_PARTICIPANT = "SELECT * from PARTICIPANT WHERE user_id=? and stream_id=?";

	public static final String SELECT_ADMIN = "SELECT * from PARTICIPANT WHERE user_id=? and stream_id=? and admin=true";

	public static final String DELETE_STREAM = "DELETE FROM stream WHERE id=?";

	public static final String DELETE_PARTICIPANT = "DELETE FROM stream WHERE stream_id=? and user_id=?";

	PreparedStatement statement = null;

	protected StreamDAO() {
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
		statement.setString(12, "/Images/empty.gif");
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
		statement = connection.prepareStatement(DELETE_STREAM);
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
			result.setThumbPath(res.getString(COLUMN_THUMB_PATH));
		}
		return result;
	}

	private void updateInvitedParticipant(IStream aStream) throws SQLException {
		// Store invited participants
		// TODO: A DAO should not call another DAO internally. 
		// The code below should be extracted in top level service class
		InvitedParticipantDAO ipDAO = new InvitedParticipantDAO();
		ipDAO.setConnection(connection);

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
		// TODO: A DAO should not call another DAO internally. 
		// The code below should be extracted in top level service class
		TagDAO tagDAO = new TagDAO();
		tagDAO.setConnection(connection);

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

	public void join(long streamId, long userId) throws SQLException {
		createParticipant(streamId, userId, false);
	}

	public void joinAsAdmin(long streamId, long userId) throws SQLException {
		createParticipant(streamId, userId, true);
	}

	private void createParticipant(long streamId, long userId, boolean admin) throws SQLException {
		statement = connection.prepareStatement(INSERT_NEW_PARTICPANT, Statement.RETURN_GENERATED_KEYS);
		statement.setLong(1, userId);
		statement.setLong(2, streamId);
		statement.setBoolean(3, admin);
		statement.executeUpdate();
	}

	public void quit(long streamId, long userId) throws SQLException {
		statement = connection.prepareStatement(DELETE_STREAM);
		statement.setLong(1, streamId);
		statement.setLong(2, userId);
		statement.executeUpdate();
	}

	public boolean isParticipant(long userId, long streamId) throws SQLException {
		statement = connection.prepareStatement(SELECT_PARTICIPANT);
		statement.setLong(1, userId);
		statement.setLong(2, streamId);
		ResultSet result = statement.executeQuery();
		return result.next();
	}

	public boolean isAdministrator(long userId, long streamId) throws SQLException {
		statement = connection.prepareStatement(SELECT_ADMIN);
		statement.setLong(1, userId);
		statement.setLong(2, streamId);
		ResultSet result = statement.executeQuery();
		return result.next();
	}

	public List<IStream> search(String query) throws SQLException {
		List<IStream> result = new ArrayList<IStream>();
		statement = connection.prepareStatement(SELECT_STREAM_VIEW);
		ResultSet res = statement.executeQuery();
		Stream aStream;
		while (res.next()) {
			aStream = new Stream();
			aStream.setId(res.getLong(COLUMN_ID));
			aStream.setTitle(res.getString(COLUMN_TITLE));
			aStream.setPublicc(res.getBoolean(COLUMN_PUBLIC));
			aStream.setOpen(res.getBoolean(COLUMN_OPEN));
			aStream.setThumbPath(res.getString(COLUMN_THUMB_PATH));
			aStream.setNumberOfParticipant(res.getInt(COLUMN_NB_OF_PARTICIPANT));
			result.add(aStream);
		}
		return result;
	}
}
