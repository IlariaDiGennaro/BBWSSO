package com.app.dto;

import java.io.Serializable;
import java.util.List;

public class BlockchainDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _id;
	private List<BlockDTO> blocks;
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}
	public List<BlockDTO> getBlocks() {
		return blocks;
	}
	public void setBlocks(List<BlockDTO> blocks) {
		this.blocks = blocks;
	}
	
	public BlockchainDTO() {

	}
	
}
