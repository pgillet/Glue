package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.glue.struct.IMedia;
import com.glue.struct.impl.Media;

/**
 * DAO for Media operations.
 * 
 * @author Greg
 * 
 */
public class MediaDAO extends AbstractDAO implements IDAO<IMedia> {

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

	public static final String UPDATE_MEDIA = "UPDATE MEDIA SET STREAM_ID=?, USER_ID=?, EXTENSION=?, MIME_TYPE=?, CAPTION=?, LATITUDE=?, LONGITUDE=?, CREATION_DATE=?, URL=?, EXTERNAL=? WHERE ID=?";

	public static final String SELECT_MEDIAS_STREAM = "SELECT * from MEDIA WHERE stream_id=?";

	public static final String SELECT_MEDIA = "SELECT ID from MEDIA WHERE stream_id=? and url=?";

	public static final String DELETE_MEDIA = "DELETE FROM MEDIA WHERE id=?";

	private PreparedStatement createStmt = null;
	private PreparedStatement updateStmt = null;
	private PreparedStatement deleteStmt = null;
	private PreparedStatement selectStmt = null;
	private PreparedStatement existStmt = null;

	protected MediaDAO() {
	}

	@Override
	public void setConnection(Connection connection) throws SQLException {
		super.setConnection(connection);

		createStmt = connection.prepareStatement(CREATE_MEDIA,
				Statement.RETURN_GENERATED_KEYS);
		updateStmt = connection.prepareStatement(UPDATE_MEDIA);
		deleteStmt = connection.prepareStatement(DELETE_MEDIA);
		selectStmt = connection.prepareStatement(SELECT_MEDIAS_STREAM);
		existStmt = connection.prepareStatement(SELECT_MEDIA);
	}

	public IMedia create(IMedia media) throws SQLException {
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

		return media;
	}

	public void delete(long mediaId) throws SQLException {
		deleteStmt.setLong(1, mediaId);
		deleteStmt.executeUpdate();
	}

	public List<IMedia> search(long streamId) throws SQLException {
		List<IMedia> result = new ArrayList<IMedia>();
		selectStmt.setLong(1, streamId);
		ResultSet res = selectStmt.executeQuery();
		IMedia media = null;
		while (res.next()) {
			media = new Media();
			media.setId(res.getLong(COLUMN_ID));
			media.setUrl(res.getString(COLUMN_URL));
			media.setCaption(res.getString(COLUMN_CAPTION));
			media.setExtension(res.getString(COLUMN_EXTENSION));
			media.setLatitude(res.getDouble(COLUMN_LATITUDE));
			media.setLongitude(res.getDouble(COLUMN_LONGITUDE));
			media.setMimeType(res.getString(COLUMN_MIME_TYPE));
			media.setCreationDate(res.getLong(COLUMN_CREATION_DATE));
			result.add(media);
		}
		return result;
	}

	/**
	 * Return true if the media already exist in db.
	 * 
	 * @param media
	 * @return
	 * @throws SQLException
	 */
	public boolean exist(IMedia media) throws SQLException {
		existStmt.setLong(1, media.getStream().getId());
		existStmt.setString(2, media.getUrl());
		ResultSet res = existStmt.executeQuery();
		return res.next();
	}

	@Override
	public void update(IMedia media) throws SQLException {
		updateStmt.setLong(1, media.getStream().getId());
		if (media.getUser() != null) {
			updateStmt.setLong(2, media.getUser().getId());
		} else {
			updateStmt.setNull(2, java.sql.Types.BIGINT);
		}
		if (media.getExtension() != null) {
			updateStmt.setString(3, media.getExtension());
		} else {
			updateStmt.setNull(3, java.sql.Types.VARCHAR);
		}
		updateStmt.setString(4, media.getMimeType());
		updateStmt.setString(5, media.getCaption());
		if (media.getLatitude() != null) {
			updateStmt.setDouble(6, media.getLatitude());
		} else {
			updateStmt.setNull(6, java.sql.Types.DECIMAL);
		}
		if (media.getLongitude() != null) {
			updateStmt.setDouble(7, media.getLongitude());
		} else {
			updateStmt.setNull(7, java.sql.Types.DECIMAL);
		}
		if (media.getCreationDate() != null) {
			updateStmt.setLong(8, media.getCreationDate());
		} else {
			updateStmt.setNull(8, java.sql.Types.BIGINT);
		}
		updateStmt.setString(9, media.getUrl());
		updateStmt.setBoolean(10, media.isExternal());

		updateStmt.executeUpdate();
	}
}
