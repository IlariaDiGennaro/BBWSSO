package com.app.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

public class HeaderDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HeaderDTO() {
		super();
	}
	
//	@Field("prevHash")
	private String prevHash;
//	@Field("dataHash")
	private String dataHash;
//	@Field("timeStamp")
	private Date timeStamp;
//	@Field("nonce")
	private Integer nonce;

	public String getPrevHash() {
		return prevHash;
	}
	public void setPrevHash(String prevHash) {
		this.prevHash = prevHash;
	}
	public String getDataHash() {
		return dataHash;
	}
	public void setDataHash(String dataHash) {
		this.dataHash = dataHash;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Integer getNonce() {
		return nonce;
	}
	public void setNonce(Integer nonce) {
		this.nonce = nonce;
	}

}
