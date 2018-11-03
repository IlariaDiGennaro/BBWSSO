package com.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.app.dto.ApplicationDTO;

public class ApplicationDAO {
	
	private DataSource dataSource;
	
	public DataSource getDataSource() {
		return dataSource;
	}



	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}



	public ApplicationDTO findByAppId(String appID) {
		ApplicationDTO application = null;
		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement("select * from application where app_id = ?");
			ps.setString(1, appID);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				application = new ApplicationDTO();
				application.setId(rs.getLong("id"));
				application.setAppID(rs.getString("app_id"));
				application.setCertificate(rs.getBlob("x509_certificate"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return application;
	}
}
