package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.glue.struct.IVenue;
import com.glue.struct.impl.Venue;

public class VenueDAO extends AbstractDAO {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_URL = "url";

	// CRUD
	public static final String CREATE_VENUE = "INSERT INTO VENUE(name, latitude, longitude, address, url) VALUES (?,?,?,?,?)";

	public static final String UPDATE_VENUE = "UPDATE VENUE SET name=?, latitude=?, longitude=?, address=?, url=? WHERE id=?";

	public static final String SELECT_VENUE_BY_ID = "SELECT * FROM VENUE WHERE ID=?";

	public static final String SELECT_VENUE_BY_ADDRESS = "SELECT * FROM VENUE WHERE ADDRESS=?";

	public static final String DELETE_VENUE = "DELETE FROM VENUE WHERE id=?";

	private PreparedStatement createStmt = null;
	private PreparedStatement updateStmt = null;
	private PreparedStatement deleteStmt = null;
	private PreparedStatement searchByIdStmt = null;
	private PreparedStatement searchByAddressStmt = null;

	@Override
	public void setConnection(Connection connection) throws SQLException {
		super.setConnection(connection);
		// Init prepared statements
		this.createStmt = connection.prepareStatement(CREATE_VENUE,
				Statement.RETURN_GENERATED_KEYS);
		this.updateStmt = connection.prepareStatement(UPDATE_VENUE);
		this.deleteStmt = connection.prepareStatement(DELETE_VENUE);
		this.searchByIdStmt = connection.prepareStatement(SELECT_VENUE_BY_ID);
		this.searchByAddressStmt = connection
				.prepareStatement(SELECT_VENUE_BY_ADDRESS);
	}

	public IVenue create(IVenue venue) throws SQLException {
		createStmt.setString(1, venue.getName());
		createStmt.setDouble(2, venue.getLatitude());
		createStmt.setDouble(3, venue.getLongitude());
		createStmt.setString(4, venue.getAddress());
		createStmt.setString(5, venue.getUrl());
		createStmt.executeUpdate();

		ResultSet rs = createStmt.getGeneratedKeys();
		if (rs.next()) {
			long key = rs.getLong(1);
			venue.setId(key);
		}

		return venue;
	}

	public void update(IVenue venue) throws SQLException {
		updateStmt.setString(1, venue.getName());
		updateStmt.setDouble(2, venue.getLatitude());
		updateStmt.setDouble(3, venue.getLongitude());
		updateStmt.setString(4, venue.getAddress());
		updateStmt.setString(5, venue.getUrl());
		updateStmt.executeUpdate();
	}

	public IVenue search(long id) throws SQLException {
		IVenue result = null;
		searchByIdStmt.setLong(1, id);
		ResultSet res = searchByIdStmt.executeQuery();
		if (res.next()) {
			result = new Venue();
			result.setId(res.getLong(COLUMN_ID));
			result.setName(res.getString(COLUMN_NAME));
			result.setLatitude(res.getDouble(COLUMN_LATITUDE));
			result.setLongitude(res.getDouble(COLUMN_LONGITUDE));
			result.setAddress(res.getString(COLUMN_ADDRESS));
			result.setUrl(res.getString(COLUMN_URL));
		}
		return result;
	}

	/**
	 * Here, we suppose that addresses are stored in a standard format.
	 * 
	 * @param address
	 * @return
	 */
	public IVenue search(String address) throws SQLException {
		IVenue result = null;
		searchByAddressStmt.setString(1, address);
		ResultSet res = searchByAddressStmt.executeQuery();
		if (res.next()) {
			result = new Venue();
			result.setId(res.getLong(COLUMN_ID));
			result.setName(res.getString(COLUMN_NAME));
			result.setLatitude(res.getDouble(COLUMN_LATITUDE));
			result.setLongitude(res.getDouble(COLUMN_LONGITUDE));
			result.setAddress(res.getString(COLUMN_ADDRESS));
			result.setUrl(res.getString(COLUMN_URL));
		}
		return result;
	}

	public void delete(long id) throws SQLException {
		deleteStmt.setLong(1, id);
		deleteStmt.executeUpdate();
	}

}
