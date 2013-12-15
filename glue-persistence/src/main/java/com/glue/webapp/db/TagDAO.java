package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DAO for User operations.
 * 
 * @author Greg
 * 
 */
public class TagDAO extends AbstractDAO {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_MAIL = "email";
	public static final String COLUMN_STREAM_ID = "stream_id";

	public static final String SELECT_TAG = "SELECT id FROM TAG WHERE tag=?";
	public static final String INSERT_NEW_TAG = "INSERT IGNORE INTO TAG(tag) VALUES (?)";
	public static final String INSERT_NEW_STREAM_TAG = "INSERT IGNORE INTO STREAM_TAG(tag_id,stream_id) VALUES (?,?)";
	public static final String DELETE_ALL_STREAM_TAG = "DELETE FROM STREAM_TAG WHERE stream_id=?";

	PreparedStatement selectTagStmt = null;
	PreparedStatement createTagStmt = null;
	PreparedStatement createStreamTagStmt = null;
	PreparedStatement deleteStreamTagStmt = null;

	protected TagDAO() {
	}

	@Override
	public void setConnection(Connection connection) throws SQLException {
		super.setConnection(connection);

		this.createTagStmt = connection.prepareStatement(INSERT_NEW_TAG,
				Statement.RETURN_GENERATED_KEYS);
		this.createStreamTagStmt = connection
				.prepareStatement(INSERT_NEW_STREAM_TAG);
		this.selectTagStmt = connection.prepareStatement(SELECT_TAG);
		this.deleteStreamTagStmt = connection
				.prepareStatement(DELETE_ALL_STREAM_TAG);
	}

	/**
	 * Creates the given tag if it does not already exist, and associates it to
	 * the stream with the given id.
	 * 
	 * @param aTag
	 * @param streamId
	 * @throws SQLException
	 */
	public void addTag(String aTag, long streamId) throws SQLException {

		Long tagId = null;
		ResultSet result;

		// Tag already exist?
		selectTagStmt.setString(1, aTag);
		result = selectTagStmt.executeQuery();
		if (result.next()) {
			tagId = result.getLong(1);
		} else {

			// Tag does not exist, create it
			createTagStmt.setString(1, aTag);
			createTagStmt.executeUpdate();

			// Get the generated id
			result = createTagStmt.getGeneratedKeys();
			if (result.next()) {
				tagId = result.getLong(1);
			}
		}

		// Update STREAM_TAG table
		createStreamTagStmt.setLong(1, tagId);
		createStreamTagStmt.setLong(2, streamId);
		createStreamTagStmt.executeUpdate();
	}

	public void deleteAll(long streamId) throws SQLException {
		deleteStreamTagStmt.setLong(1, streamId);
		deleteStreamTagStmt.executeUpdate();
	}
}
