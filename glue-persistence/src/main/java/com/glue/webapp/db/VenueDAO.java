package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.glue.domain.IVenue;
import com.glue.domain.impl.Venue;

public class VenueDAO extends AbstractDAO implements IDAO<IVenue> {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_URL = "url";
	public static final String COLUMN_CITY = "city";

	// CRUD
	public static final String CREATE_VENUE = "INSERT INTO VENUE(name, latitude, longitude, address, url, city) VALUES (?,?,?,?,?,?)";

	public static final String UPDATE_VENUE = "UPDATE VENUE SET name=?, latitude=?, longitude=?, address=?, url=?, city=? WHERE id=?";

	public static final String SELECT_VENUE_BY_ID = "SELECT * FROM VENUE WHERE ID=?";

	public static final String SELECT_VENUE_BY_NAME_AND_CITY = "SELECT * FROM VENUE WHERE NAME=? and CITY=?";

	public static final String SELECT_VENUE_BY_NAME = "SELECT * FROM VENUE WHERE NAME=?";

	public static final String DELETE_VENUE = "DELETE FROM VENUE WHERE id=?";

	private PreparedStatement createStmt = null;
	private PreparedStatement updateStmt = null;
	private PreparedStatement deleteStmt = null;
	private PreparedStatement searchByIdStmt = null;
	private PreparedStatement searchForDuplicateStmt = null;
	private PreparedStatement searchByNameStmt = null;

	protected VenueDAO() {
	}

	@Override
	public void setConnection(Connection connection) throws SQLException {
		super.setConnection(connection);
		// Init prepared statements
		this.createStmt = connection.prepareStatement(CREATE_VENUE, Statement.RETURN_GENERATED_KEYS);
		this.updateStmt = connection.prepareStatement(UPDATE_VENUE);
		this.deleteStmt = connection.prepareStatement(DELETE_VENUE);
		this.searchByIdStmt = connection.prepareStatement(SELECT_VENUE_BY_ID);
		this.searchForDuplicateStmt = connection.prepareStatement(SELECT_VENUE_BY_NAME_AND_CITY);
		this.searchByNameStmt = connection.prepareStatement(SELECT_VENUE_BY_NAME);
	}

	public IVenue create(IVenue venue) throws SQLException {
		createStmt.setString(1, venue.getName());
		createStmt.setDouble(2, venue.getLatitude());
		createStmt.setDouble(3, venue.getLongitude());
		createStmt.setString(4, venue.getAddress());
		createStmt.setString(5, venue.getUrl());
		createStmt.setString(6, venue.getCity());
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
		createStmt.setString(6, venue.getCity());
		createStmt.setLong(7, venue.getId());
		updateStmt.executeUpdate();
	}

	public IVenue search(long id) throws SQLException {
		IVenue result = null;
		searchByIdStmt.setLong(1, id);
		ResultSet res = searchByIdStmt.executeQuery();
		if (res.next()) {
			result = populateVenue(res);
		}
		return result;
	}

	/**
	 * Search for an existing venue: Name + City
	 * 
	 * @param venue
	 * @return
	 */
	public IVenue searchForDuplicate(IVenue venue) throws SQLException {
		IVenue result = null;
		searchForDuplicateStmt.setString(1, venue.getName());
		searchForDuplicateStmt.setString(2, venue.getCity());
		ResultSet res = searchForDuplicateStmt.executeQuery();
		if (res.next()) {
			result = populateVenue(res);
		}
		return result;
	}

	/**
	 * Retrieves the venue value in the current row of the given ResultSet
	 * object. The caller must ensure that the current row is valid and that the
	 * ResultSet is not closed.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private IVenue populateVenue(ResultSet rs) throws SQLException {
		IVenue result;
		result = new Venue();
		result.setId(rs.getLong(COLUMN_ID));
		result.setName(rs.getString(COLUMN_NAME));
		result.setLatitude(rs.getDouble(COLUMN_LATITUDE));
		result.setLongitude(rs.getDouble(COLUMN_LONGITUDE));
		result.setAddress(rs.getString(COLUMN_ADDRESS));
		result.setUrl(rs.getString(COLUMN_URL));
		result.setCity(rs.getString(COLUMN_CITY));
		return result;
	}

	public void delete(long id) throws SQLException {
		deleteStmt.setLong(1, id);
		deleteStmt.executeUpdate();
	}

}
