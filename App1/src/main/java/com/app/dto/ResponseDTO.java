package com.app.dto;

import java.io.Serializable;

public class ResponseDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sender;
	private String receiver;
	private boolean validBlock;
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public boolean isValidBlock() {
		return validBlock;
	}
	public void setValidBlock(boolean validBlock) {
		this.validBlock = validBlock;
	}
	
	
}
