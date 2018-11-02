package com.app.dto;

import java.io.Serializable;

public class MessageDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sender;
	private String receiver;
	private String encryptedBlock;
	private String encryptedSimmetricKey;
	
	public MessageDTO() {
		super();
	}

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

	public String getEncryptedBlock() {
		return encryptedBlock;
	}

	public void setEncryptedBlock(String encryptedBlock) {
		this.encryptedBlock = encryptedBlock;
	}

	public String getEncryptedSimmetricKey() {
		return encryptedSimmetricKey;
	}

	public void setEncryptedSimmetricKey(String encryptedSimmetricKey) {
		this.encryptedSimmetricKey = encryptedSimmetricKey;
	}
	
}
