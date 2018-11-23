package com.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.dto.ApplicationDTO;

public class ApplicationDAO {
	
	private static Logger logger = LoggerFactory.getLogger(ApplicationDAO.class);
	
	private DataSource dataSource;
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private static final String R_Application_AppID = "select * from application where app_id = ?";

	public ApplicationDTO readByAppId(String appID) throws Exception {
		ApplicationDTO application = null;
		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement(R_Application_AppID);
			ps.setString(1, appID);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				application = new ApplicationDTO();
				application.setId(rs.getLong("id"));
				application.setAppID(rs.getString("app_id"));
				application.setCertificate(rs.getBlob("x509_certificate"));
			}
			rs.close();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			logger.error("ApplicationDAO", e);
			throw new Exception(e);
		}
		return application;
	}
}
