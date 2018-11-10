package com.app.dto;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Field;

public class BodyDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	@Field("userName")
	private String userName;
//	@Field("appID")
	private String appID;

	public BodyDTO() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	
	
}
