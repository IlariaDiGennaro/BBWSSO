package com.app.dto;

import java.io.Serializable;
import java.sql.Blob;

public class ApplicationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Long id;
	private String appID;
	private Blob certificate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAppID() {
		return appID;
	}
	public void setAppID(String appID) {
		this.appID = appID;
	}
	public Blob getCertificate() {
		return certificate;
	}
	public void setCertificate(Blob certificate) {
		this.certificate = certificate;
	}

}
