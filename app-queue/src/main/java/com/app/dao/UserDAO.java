package com.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.dto.UserDTO;

public class UserDAO {
	
	private static Logger logger = LoggerFactory.getLogger(UserDAO.class);
	
	private DataSource dataSource;
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private static final String R_User_Username = "select *	from user where username = ?";

	public UserDTO readByUsername(String username) throws Exception {
		UserDTO user = null;
		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement(R_User_Username);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				user = new UserDTO();
				user.setId(rs.getLong("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setApps(rs.getString("apps"));
			}
		} catch (SQLException e) {
			logger.error("UserDAO", e);
			throw new Exception(e);
		}
		return user;
	}
}
