package com.glue.webapp.db;

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

	PreparedStatement statement = null;

	protected TagDAO() {
	}

	public void create(String aTag, long streamId) throws SQLException {

		Long tagId = null;
		ResultSet result;

		// Tag already exist?
		statement = connection.prepareStatement(SELECT_TAG);
		statement.setString(1, aTag);
		result = statement.executeQuery();
		if (result.next()) {
			tagId = result.getLong(1);
		} else {

			// Tag does not exist, create it
			statement = connection.prepareStatement(INSERT_NEW_TAG, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, aTag);
			statement.executeUpdate();

			// Get the generated id
			result = statement.getGeneratedKeys();
			while (result.next()) {
				tagId = result.getLong(1);
			}
		}

		// Update STREAM_TAG table
		statement = connection.prepareStatement(INSERT_NEW_STREAM_TAG);
		statement.setLong(1, tagId);
		statement.setLong(2, streamId);
		statement.executeUpdate();

	}

	public void deleteAll(long streamId) throws SQLException {
		statement = connection.prepareStatement(DELETE_ALL_STREAM_TAG);
		statement.setLong(1, streamId);
		statement.executeUpdate();
	}
}
