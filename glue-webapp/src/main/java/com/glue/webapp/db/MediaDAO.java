package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.glue.struct.IMedia;

/**
 * DAO for Media operations.
 * 
 * @author Greg
 * 
 */
public class MediaDAO extends AbstractDAO {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_STREAM_ID = "stream_id";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_EXTENSION = "extension";
	public static final String COLUMN_MIME_TYPE = "mime_type";
	public static final String COLUMN_CAPTION = "caption";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_CREATION_DATE = "creation_date";
	public static final String COLUMN_EXTERNAL = "external";
	public static final String COLUMN_URL = "url";

	public static final String CREATE_MEDIA = "INSERT INTO MEDIA(stream_id, user_id, extension, "
			+ "mime_type, caption, latitude, longitude, creation_date, url, external) VALUES (?,?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_MEDIAS_STREAM = "SELECT * from MEDIA WHERE stream_id=?";

	public static final String DELETE_MEDIA = "DELETE FROM MEDIA WHERE id=?";

	private PreparedStatement createStmt = null;
	private PreparedStatement deleteStmt = null;
	private PreparedStatement search = null;

	protected MediaDAO() {
	}

	@Override
	public void setConnection(Connection connection) throws SQLException {
		super.setConnection(connection);

		createStmt = connection.prepareStatement(CREATE_MEDIA, Statement.RETURN_GENERATED_KEYS);
		deleteStmt = connection.prepareStatement(DELETE_MEDIA);
	}

	public void create(IMedia media) throws SQLException {
		createStmt.setLong(1, media.getStream().getId());
		if (media.getUser() != null) {
			createStmt.setLong(2, media.getUser().getId());
		} else {
			createStmt.setNull(2, java.sql.Types.BIGINT);
		}
		if (media.getExtension() != null) {
			createStmt.setString(3, media.getExtension());
		} else {
			createStmt.setNull(3, java.sql.Types.VARCHAR);
		}
		createStmt.setString(4, media.getMimeType());
		createStmt.setString(5, media.getCaption());
		if (media.getLatitude() != null) {
			createStmt.setDouble(6, media.getLatitude());
		} else {
			createStmt.setNull(6, java.sql.Types.DECIMAL);
		}
		if (media.getLongitude() != null) {
			createStmt.setDouble(7, media.getLongitude());
		} else {
			createStmt.setNull(7, java.sql.Types.DECIMAL);
		}
		if (media.getCreationDate() != null) {
			createStmt.setLong(8, media.getCreationDate());
		} else {
			createStmt.setNull(8, java.sql.Types.BIGINT);
		}
		createStmt.setString(9, media.getUrl());
		createStmt.setBoolean(10, media.isExternal());

		createStmt.executeUpdate();

		// Get the generated id
		ResultSet result = createStmt.getGeneratedKeys();
		Long id = null;
		while (result.next()) {
			id = result.getLong(1);
		}
		media.setId(id);
	}

	public void delete(long mediaId) throws SQLException {
		deleteStmt.setLong(1, mediaId);
		deleteStmt.executeUpdate();
	}

	/*
	 * public Set<IMedia> search(long streamId) throws SQLException {
	 * Set<IMedia> result = new HashSet<IMedia>(); statement =
	 * connection.prepareStatement(SELECT_MEDIAS_STREAM,
	 * Statement.RETURN_GENERATED_KEYS); statement.setLong(1, streamId);
	 * ResultSet res = statement.executeQuery(); IMedia media = null; while
	 * (res.next()) { media = new Media(); media.setId(res.getLong(COLUMN_ID));
	 * media.setCaption(res.getString(COLUMN_CAPTION));
	 * media.setExtension(res.getString(COLUMN_EXTENSION));
	 * media.setLatitude(res.getDouble(COLUMN_LATITUDE));
	 * media.setLongitude(res.getDouble(COLUMN_LONGITUDE));
	 * media.setMimeType(res.getString(COLUMN_MIME_TYPE));
	 * media.setStartDate(res.getLong(COLUMN_START_DATE)); result.add(media); }
	 * return result.size() > 0 ? result : null; }
	 */

}
