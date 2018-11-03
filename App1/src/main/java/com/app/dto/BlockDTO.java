package com.app.dto;

import java.io.Serializable;

public class BlockDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HeaderDTO header;
	private BodyDTO body;
	
	public BlockDTO() {
		super();
	}
	public HeaderDTO getHeader() {
		return header;
	}
	public void setHeader(HeaderDTO header) {
		this.header = header;
	}
	public BodyDTO getBody() {
		return body;
	}
	public void setBody(BodyDTO body) {
		this.body = body;
	}
	
}
