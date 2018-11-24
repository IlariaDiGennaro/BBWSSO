package com.app.dto;

import java.io.Serializable;
import java.util.Date;

public class HeaderDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HeaderDTO() {
		super();
	}
	
	private String prevHash;
	private String dataHash;
	private String timeStamp;
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
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Integer getNonce() {
		return nonce;
	}
	public void setNonce(Integer nonce) {
		this.nonce = nonce;
	}

}
