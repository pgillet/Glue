package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.glue.struct.IStream;
import com.glue.struct.IVenue;
import com.glue.struct.impl.InvitedParticipant;
import com.glue.struct.impl.Stream;
import com.glue.struct.impl.Venue;

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
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_URL = "url";
	public static final String COLUMN_PUBLIC = "public";
	public static final String COLUMN_OPEN = "open";
	public static final String COLUMN_SECRET_QUESTION = "secret_question";
	public static final String COLUMN_SECRET_ANSWER = "secret_answer";
	public static final String COLUMN_REQUEST_TO_PARTICPATE = "request_to_participate";
	public static final String COLUMN_START_DATE = "start_date";
	public static final String COLUMN_END_DATE = "end_date";
	public static final String COLUMN_NB_OF_PARTICIPANT = "nb_of_participant";
	public static final String COLUMN_THUMB_PATH = "thumb_path";
	public static final String COLUMN_VENUE_ID = "venue_id";
	
	public static final String CREATE_STREAM = "INSERT INTO STREAM(TITLE, DESCRIPTION, URL, PUBLIC, OPEN, "
			+ "SECRET_QUESTION, SECRET_ANSWER, REQUEST_TO_PARTICIPATE, START_DATE, END_DATE, "
			+ "THUMB_PATH, VENUE_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_NEW_PARTICIPANT = "INSERT IGNORE INTO PARTICIPANT(user_id, stream_id, admin) VALUES (?,?,?)";

	public static final String UPDATE_STREAM = "UPDATE STREAM SET TITLE=?, DESCRIPTION=?, URL=?, PUBLIC=?, OPEN=?, "
			+ "SECRET_QUESTION=?, SECRET_ANSWER=?, REQUEST_TO_PARTICIPATE=?, START_DATE=?, END_DATE=?, VENUE_ID=? "
			+ "WHERE ID=?";

	public static final String SELECT_STREAM_BY_ID = "SELECT * FROM STREAM WHERE ID=?";

	public static final String SELECT_STREAM_BY_IDS = "SELECT * FROM STREAM WHERE ID IN (?)";

	public static final String SELECT_STREAM_VIEW = "SELECT * from STREAM_VIEW";

	public static final String SELECT_PARTICIPANT = "SELECT * from PARTICIPANT WHERE user_id=? and stream_id=?";

	public static final String SELECT_ADMIN = "SELECT * from PARTICIPANT WHERE user_id=? and stream_id=? and admin=true";

	public static final String DELETE_STREAM = "DELETE FROM stream WHERE id=?";

	public static final String DELETE_PARTICIPANT = "DELETE FROM stream WHERE stream_id=? and user_id=?";

	private PreparedStatement createStmt = null;
	private PreparedStatement updateStmt = null;
	private PreparedStatement deleteStmt = null;
	private PreparedStatement searchByIdStmt = null;

	protected StreamDAO() {
	}

	@Override
	public void setConnection(Connection connection) throws SQLException {
		super.setConnection(connection);

		this.createStmt = connection.prepareStatement(CREATE_STREAM,
				Statement.RETURN_GENERATED_KEYS);
		this.updateStmt = connection.prepareStatement(UPDATE_STREAM);
		this.deleteStmt = connection.prepareStatement(DELETE_STREAM);
		this.searchByIdStmt = connection.prepareStatement(SELECT_STREAM_BY_ID);
	}

	public void create(IStream aStream) throws SQLException {

		checkVenue(aStream);

		createStmt.setString(1, aStream.getTitle());
		createStmt.setString(2, aStream.getDescription());
		createStmt.setString(3, aStream.getUrl());
		createStmt.setBoolean(4, aStream.isPublicc());
		createStmt.setBoolean(5, aStream.isOpen());
		createStmt.setString(6, aStream.getSharedSecretQuestion());
		createStmt.setString(7, aStream.getSharedSecretQuestion());
		createStmt.setBoolean(8, aStream.isShouldRequestToParticipate());
		createStmt.setLong(9, aStream.getStartDate());
		createStmt.setLong(10, aStream.getEndDate());
		createStmt.setString(11, aStream.getThumbPath());
		createStmt.setLong(12, aStream.getVenue().getId());
		createStmt.executeUpdate();

		// Get the generated id
		ResultSet result = createStmt.getGeneratedKeys();
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

	/**
	 * Check the stream integrity.
	 * 
	 * @param aStream
	 * @throws SQLException
	 */
	private void checkVenue(IStream aStream) throws SQLException {
		IVenue venue = aStream.getVenue();
		if (venue != null && venue.getId() == null) {
			throw new SQLException("A venue is set but is not persisted");
		}
	}

	public void update(IStream aStream) throws SQLException {
		checkVenue(aStream);

		updateStmt.setLong(12, aStream.getId());
		updateStmt.setString(1, aStream.getTitle());
		updateStmt.setString(2, aStream.getDescription());
		updateStmt.setString(3, aStream.getUrl());
		updateStmt.setBoolean(4, aStream.isPublicc());
		updateStmt.setBoolean(5, aStream.isOpen());
		updateStmt.setString(6, aStream.getSharedSecretQuestion());
		updateStmt.setString(7, aStream.getSharedSecretAnswer());
		updateStmt.setBoolean(8, aStream.isShouldRequestToParticipate());
		updateStmt.setLong(9, aStream.getStartDate());
		updateStmt.setLong(10, aStream.getEndDate());
		updateStmt.setLong(11, aStream.getVenue().getId());
		updateStmt.executeUpdate();

		// Invited participant
		updateInvitedParticipant(aStream);

		// Store tags
		updateTags(aStream);
	}

	public void delete(long streamId) throws SQLException {
		deleteStmt = connection.prepareStatement(DELETE_STREAM);
		deleteStmt.setLong(1, streamId);
		deleteStmt.executeUpdate();
	}

	public IStream search(long streamId) throws SQLException {
		IStream result = null;

		searchByIdStmt.setLong(1, streamId);
		ResultSet res = searchByIdStmt.executeQuery();
		if (res.next()) {
			result = new Stream();
			result.setId(res.getLong(COLUMN_ID));
			result.setTitle(res.getString(COLUMN_TITLE));
			result.setDescription(res.getString(COLUMN_DESCRIPTION));
			result.setUrl(res.getString(COLUMN_URL));
			result.setPublicc(res.getBoolean(COLUMN_PUBLIC));
			result.setOpen(res.getBoolean(COLUMN_OPEN));
			result.setSharedSecretQuestion(res
					.getString(COLUMN_SECRET_QUESTION));
			result.setSharedSecretAnswer(res.getString(COLUMN_SECRET_ANSWER));
			result.setShouldRequestToParticipate(res
					.getBoolean(COLUMN_REQUEST_TO_PARTICPATE));
			result.setStartDate(res.getLong(COLUMN_START_DATE));
			result.setEndDate(res.getLong(COLUMN_END_DATE));
			result.setThumbPath(res.getString(COLUMN_THUMB_PATH));
		}
		return result;
	}

	public List<IStream> searchInList(Long... ids) throws SQLException {
		List<IStream> streams = new ArrayList<IStream>();

		// Array array = connection.createArrayOf("BIGINT", ids); // Feature not
		// supported by MySQL

		if (ids.length > 0) {
			
			// ?,?,?,...
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < ids.length; i++) {
				sb.append('?');
				sb.append(",");
			}
			// Remove the last comma
			String params = sb.substring(0, sb.length() - 1);
			String sql = SELECT_STREAM_BY_IDS.replaceAll("\\?", params);
			
			PreparedStatement statement = connection
					.prepareStatement(sql);
			// statement.setArray(1, array);
			
			for (int i = 0; i < ids.length; i++) {
				statement.setLong(i+1, ids[i]);
			}
			
			ResultSet res = statement.executeQuery();
			while (res.next()) {
				IStream stream = new Stream();
				stream.setId(res.getLong(COLUMN_ID));
				stream.setTitle(res.getString(COLUMN_TITLE));
				stream.setDescription(res.getString(COLUMN_DESCRIPTION));
				stream.setUrl(res.getString(COLUMN_URL));
				stream.setPublicc(res.getBoolean(COLUMN_PUBLIC));
				stream.setOpen(res.getBoolean(COLUMN_OPEN));
				stream.setSharedSecretQuestion(res
						.getString(COLUMN_SECRET_QUESTION));
				stream.setSharedSecretAnswer(res
						.getString(COLUMN_SECRET_ANSWER));
				stream.setShouldRequestToParticipate(res
						.getBoolean(COLUMN_REQUEST_TO_PARTICPATE));
				stream.setStartDate(res.getLong(COLUMN_START_DATE));
				stream.setEndDate(res.getLong(COLUMN_END_DATE));
				stream.setThumbPath(res.getString(COLUMN_THUMB_PATH));
				stream.setThumbPath(res.getString(COLUMN_THUMB_PATH));
				
				IVenue dummyVenue = new Venue();
				dummyVenue.setId(res.getLong(COLUMN_VENUE_ID));
				stream.setVenue(dummyVenue);

				streams.add(stream);
			}
		}
		return streams;
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

	private void createParticipant(long streamId, long userId, boolean admin)
			throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				INSERT_NEW_PARTICIPANT, Statement.RETURN_GENERATED_KEYS);
		statement.setLong(1, userId);
		statement.setLong(2, streamId);
		statement.setBoolean(3, admin);
		statement.executeUpdate();
	}

	public void quit(long streamId, long userId) throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement(DELETE_PARTICIPANT);
		statement.setLong(1, streamId);
		statement.setLong(2, userId);
		statement.executeUpdate();
	}

	public boolean isParticipant(long userId, long streamId)
			throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement(SELECT_PARTICIPANT);
		statement.setLong(1, userId);
		statement.setLong(2, streamId);
		ResultSet result = statement.executeQuery();
		return result.next();
	}

	public boolean isAdministrator(long userId, long streamId)
			throws SQLException {
		PreparedStatement statement = connection.prepareStatement(SELECT_ADMIN);
		statement.setLong(1, userId);
		statement.setLong(2, streamId);
		ResultSet result = statement.executeQuery();
		return result.next();
	}

	public List<IStream> search(String query) throws SQLException {
		List<IStream> result = new ArrayList<IStream>();
		PreparedStatement statement = connection
				.prepareStatement(SELECT_STREAM_VIEW);
		ResultSet res = statement.executeQuery();
		IStream aStream;
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
