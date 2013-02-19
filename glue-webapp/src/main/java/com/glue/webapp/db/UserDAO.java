package com.glue.webapp.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.glue.struct.IUser;
import com.glue.struct.impl.User;

/**
 * DAO for User operations.
 * 
 * @author Greg
 * 
 */
public class UserDAO extends AbstractDAO {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_FIRST_NAME = "first_name";
	public static final String COLUMN_LAST_NAME = "last_name";
	public static final String COLUMN_MAIL = "email";
	public static final String COLUMN_PWD = "passwd";

	public static final String INSERT_NEW_USER = "INSERT INTO GLUE_USER(first_name, last_name, email, passwd) VALUES (?,?,?,?)";
	public static final String UPDATE_USER = "UPDATE GLUE_USER SET first_name=?, last_name=?, email=?, "
			+ "passwd=? WHERE id=?";

	public static final String SELECT_USER = "SELECT * from GLUE_USER WHERE id=?";
	
	public static final String AUTH_USER = "SELECT * FROM GLUE_USER WHERE email=? AND passwd=?";

	public static final String SELECT_MAIL_USER = "SELECT * from GLUE_USER WHERE email=?";

	public static final String DELETE_USER = "DELETE FROM GLUE_USER WHERE id=?";

	PreparedStatement statement = null;

	protected UserDAO() {
	}

	public void create(IUser user) throws SQLException {

		statement = connection.prepareStatement(INSERT_NEW_USER, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, user.getFirstName());
		statement.setString(2, user.getLastName());
		statement.setString(3, user.getMailAddress());
		statement.setString(4, user.getPassword());
		statement.executeUpdate();

		// Get the generated id
		ResultSet result = statement.getGeneratedKeys();
		Long id = null;
		while (result.next()) {
			id = result.getLong(1);
		}
		user.setId(id);
	}

	public void update(IUser user) throws SQLException {
		statement = connection.prepareStatement(UPDATE_USER);
		statement.setLong(5, user.getId());
		statement.setString(1, user.getFirstName());
		statement.setString(2, user.getLastName());
		statement.setString(3, user.getMailAddress());
		statement.setString(4, user.getPassword());
		statement.executeUpdate();
	}

	public IUser search(long userId) throws SQLException {
		User result = null;
		statement = connection.prepareStatement(SELECT_USER, Statement.RETURN_GENERATED_KEYS);
		statement.setLong(1, userId);
		ResultSet res = statement.executeQuery();
		if (res.next()) {
			result = new User();
			result.setId(res.getLong(COLUMN_ID));
			result.setFirstName(res.getString(COLUMN_FIRST_NAME));
			result.setLastName(res.getString(COLUMN_LAST_NAME));
			result.setMailAddress(res.getString(COLUMN_MAIL));
			result.setPassword(res.getString(COLUMN_PWD));
		}
		return result;
	}

	public IUser search(String mail) throws SQLException {
		User result = null;
		statement = connection.prepareStatement(SELECT_MAIL_USER);
		statement.setString(1, mail);
		ResultSet res = statement.executeQuery();
		if (res.next()) {
			result = new User();
			result.setId(res.getLong(COLUMN_ID));
			result.setFirstName(res.getString(COLUMN_FIRST_NAME));
			result.setLastName(res.getString(COLUMN_LAST_NAME));
			result.setMailAddress(res.getString(COLUMN_MAIL));
			result.setPassword(res.getString(COLUMN_PWD));
		}
		return result;
	}
	
	public IUser authenticate(String mail, String passwd) throws SQLException {
		User result = null;
		PreparedStatement statement = connection.prepareStatement(AUTH_USER);
		statement.setString(1, mail);
		statement.setString(2, passwd);
		ResultSet res = statement.executeQuery();
		if (res.next()) {
			result = new User();
			result.setId(res.getLong(COLUMN_ID));
			result.setFirstName(res.getString(COLUMN_FIRST_NAME));
			result.setLastName(res.getString(COLUMN_LAST_NAME));
			result.setMailAddress(res.getString(COLUMN_MAIL));
			result.setPassword(res.getString(COLUMN_PWD));
		}
		return result;
	}

}
