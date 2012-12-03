package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.glue.struct.impl.dto.UserDTO;

/**
 * DAO for User operations.
 * 
 * @author Greg
 * 
 */
public class UserDAO {

	public static final String INSERT_NEW_USER = "INSERT INTO USER(first_name, last_name, email, passwd) VALUES (?,?,?,?)";

	Connection connection = null;
	PreparedStatement statement = null;

	public UserDAO(Connection connection) {
		this.connection = connection;
	}

	public void create(UserDTO user) throws SQLException {

		statement = connection.prepareStatement(INSERT_NEW_USER, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, user.getFirstName());
		statement.setString(2, user.getLastName());
		statement.setString(3, user.getMail());
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
}
