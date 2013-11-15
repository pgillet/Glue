package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.glue.struct.ICategory;
import com.glue.struct.impl.Category;

public class CategoryDAO extends AbstractDAO {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";

	// CRUD
	public static final String CREATE_CATEGORY = "INSERT INTO CATEGORY(name) VALUES (?)";

	public static final String UPDATE_CATEGORY = "UPDATE CATEGORY SET name=? WHERE id=?";

	public static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM CATEGORY WHERE id=?";

	public static final String DELETE_CATEGORY = "DELETE FROM CATEGORY WHERE id=?";

	public static final String SELECT_ALL = "SELECT * FROM CATEGORY";

	private PreparedStatement createStmt = null;
	private PreparedStatement updateStmt = null;
	private PreparedStatement deleteStmt = null;
	private PreparedStatement searchByIdStmt = null;
	private PreparedStatement searchAllStmt = null;

	protected CategoryDAO() {
	}

	@Override
	public void setConnection(Connection connection) throws SQLException {
		super.setConnection(connection);
		// Init prepared statements
		this.createStmt = connection.prepareStatement(CREATE_CATEGORY, Statement.RETURN_GENERATED_KEYS);
		this.updateStmt = connection.prepareStatement(UPDATE_CATEGORY);
		this.deleteStmt = connection.prepareStatement(DELETE_CATEGORY);
		this.searchByIdStmt = connection.prepareStatement(SELECT_CATEGORY_BY_ID);
		this.searchAllStmt = connection.prepareStatement(SELECT_ALL);
	}

	public ICategory create(ICategory category) throws SQLException {
		createStmt.setString(1, category.getName());
		createStmt.executeUpdate();

		ResultSet rs = createStmt.getGeneratedKeys();
		if (rs.next()) {
			long key = rs.getLong(1);
			category.setId(key);
		}

		return category;
	}

	public void update(ICategory category) throws SQLException {
		updateStmt.setString(1, category.getName());
		updateStmt.executeUpdate();
	}

	public ICategory search(long id) throws SQLException {
		ICategory result = null;
		searchByIdStmt.setLong(1, id);
		ResultSet res = searchByIdStmt.executeQuery();
		if (res.next()) {
			result = new Category();
			result.setId(res.getLong(COLUMN_ID));
			result.setName(res.getString(COLUMN_NAME));
		}
		return result;
	}

	public void delete(long id) throws SQLException {
		deleteStmt.setLong(1, id);
		deleteStmt.executeUpdate();
	}

	public Set<ICategory> searchAll() throws SQLException {
		Set<ICategory> result = new HashSet<ICategory>();
		ResultSet res = searchAllStmt.executeQuery();
		ICategory category = null;
		while (res.next()) {
			category = new Category();
			category.setId(res.getLong(COLUMN_ID));
			category.setName(res.getString(COLUMN_NAME));
			result.add(category);
		}
		return result;
	}

}
