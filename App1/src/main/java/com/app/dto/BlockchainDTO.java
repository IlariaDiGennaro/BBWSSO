package com.app.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="collection")
public class BlockchainDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String _id;
//	@Field("blocks")
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
