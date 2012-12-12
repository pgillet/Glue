package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.glue.struct.IMedia;
import com.glue.struct.IUser;
import com.glue.struct.impl.Media;

/**
 * DAO for Media operations.
 * 
 * @author Greg
 * 
 */
public class MediaDAO {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_STREAM_ID = "stream_id";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_EXTENSION = "extension";
	public static final String COLUMN_MIME_TYPE = "mime_type";
	public static final String COLUMN_CAPTION = "caption";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_START_DATE = "start_date";

	public static final String INSERT_NEW_MEDIA = "INSERT INTO MEDIA(stream_id, user_id, extension, "
			+ "mime_type, caption, latitude, longitude, start_date) VALUES (?,?,?,?,?,?,?,?)";

	public static final String SELECT_MEDIAS_STREAM = "SELECT * from MEDIA WHERE stream_id=?";

	public static final String DELETE_MEDIA = "DELETE FROM MEDIA WHERE id=?";

	Connection connection = null;
	PreparedStatement statement = null;

	public MediaDAO(Connection connection) {
		this.connection = connection;
	}

	public void create(IMedia media, IUser user) throws SQLException {

		statement = connection.prepareStatement(INSERT_NEW_MEDIA, Statement.RETURN_GENERATED_KEYS);
		statement.setLong(1, media.getStreamId());
		statement.setLong(2, user.getId());
		statement.setString(3, media.getExtension());
		statement.setString(4, media.getMimeType());
		statement.setString(5, media.getCaption());
		statement.setDouble(6, media.getLatitude());
		statement.setDouble(7, media.getLongitude());
		statement.setLong(8, media.getStartDate());
		statement.executeUpdate();

		// Get the generated id
		ResultSet result = statement.getGeneratedKeys();
		Long id = null;
		while (result.next()) {
			id = result.getLong(1);
		}
		media.setId(id);
	}

	public void delete(long mediaId) throws SQLException {
		statement = connection.prepareStatement(DELETE_MEDIA);
		statement.setLong(1, mediaId);
		statement.executeUpdate();
	}

	public Set<IMedia> search(long streamId) throws SQLException {
		Set<IMedia> result = new HashSet<IMedia>();
		statement = connection.prepareStatement(SELECT_MEDIAS_STREAM, Statement.RETURN_GENERATED_KEYS);
		statement.setLong(1, streamId);
		ResultSet res = statement.executeQuery();
		IMedia media = null;
		while (res.next()) {
			media = new Media();
			media.setId(res.getLong(COLUMN_ID));
			media.setCaption(res.getString(COLUMN_CAPTION));
			media.setExtension(res.getString(COLUMN_EXTENSION));
			media.setLatitude(res.getDouble(COLUMN_LATITUDE));
			media.setLongitude(res.getDouble(COLUMN_LONGITUDE));
			media.setMimeType(res.getString(COLUMN_MIME_TYPE));
			media.setStartDate(res.getLong(COLUMN_START_DATE));
			result.add(media);
		}
		return result.size() > 0 ? result : null;
	}

}