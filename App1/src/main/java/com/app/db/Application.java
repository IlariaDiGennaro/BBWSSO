package com.app.db;

import java.io.Serializable;
import java.sql.Blob;

//@Entity
//@Table(name="application")
public class Application implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	@Id
//	@Column(name="id")
	private Long id;
//	@Column(name="app_id")
	private String appID;
//	@Column(name="x509_certificate")
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
