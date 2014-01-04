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
public class UserDAO extends AbstractDAO implements IDAO<IUser> {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_MAIL = "email";
	public static final String COLUMN_PWD = "passwd";

	public static final String INSERT_NEW_USER = "INSERT INTO GLUE_USER(name, email, passwd) VALUES (?,?,?)";
	public static final String UPDATE_USER = "UPDATE GLUE_USER SET name=?, email=?, "
			+ "passwd=? WHERE id=?";

	public static final String SELECT_USER = "SELECT * from GLUE_USER WHERE id=?";
	
	public static final String AUTH_USER = "SELECT * FROM GLUE_USER WHERE email=? AND passwd=?";

	public static final String SELECT_MAIL_USER = "SELECT * from GLUE_USER WHERE email=?";

	public static final String DELETE_USER = "DELETE FROM GLUE_USER WHERE id=?";

	PreparedStatement statement = null;

	protected UserDAO() {
	}

	public IUser create(IUser user) throws SQLException {

		statement = connection.prepareStatement(INSERT_NEW_USER, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, user.getName());
		statement.setString(2, user.getMailAddress());
		statement.setString(3, user.getPassword());
		statement.executeUpdate();

		// Get the generated id
		ResultSet result = statement.getGeneratedKeys();
		Long id = null;
		while (result.next()) {
			id = result.getLong(1);
		}
		user.setId(id);
		
		return user;
	}

	public void update(IUser user) throws SQLException {
		statement = connection.prepareStatement(UPDATE_USER);
		statement.setString(1, user.getName());
		statement.setString(2, user.getMailAddress());
		statement.setString(3, user.getPassword());
		statement.setLong(4, user.getId());
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
			result.setName(res.getString(COLUMN_NAME));
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
			result.setName(res.getString(COLUMN_NAME));
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
			result.setName(res.getString(COLUMN_NAME));
			result.setMailAddress(res.getString(COLUMN_MAIL));
			result.setPassword(res.getString(COLUMN_PWD));
		}
		return result;
	}

	@Override
	public void delete(long id) throws SQLException {
		statement = connection.prepareStatement(DELETE_USER);
		statement.setLong(1, id);
		statement.executeUpdate();
	}

}
