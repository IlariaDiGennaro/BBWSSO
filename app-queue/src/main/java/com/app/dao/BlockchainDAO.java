package com.app.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.app.dto.BlockDTO;
import com.app.dto.BlockchainDTO;
import com.app.properties.AppProperties;
import com.app.utilities.AppUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class BlockchainDAO {
	private MongoClient mongoClient;

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}
	
	public boolean createUserBlockchain(BlockDTO genesisBlock,String receiver) {
		boolean createdBC = false;
		//String mdbName = AppProperties.getMongodbPrefix().concat(genesisBlock.getBody().getAppID()); //MDBApp1
		String mdbName = AppProperties.getMongodbPrefix().concat(receiver);
		// TODO mdbName pass receiver because appID is null
		// TODO il genesis block non deve controllare l'accesso ad app id perchè è solo login
		MongoDatabase mdb = mongoClient.getDatabase(mdbName);
		MongoCollection<Document> actualCollection = mdb.getCollection(AppProperties.getMongodbCollectionActual());
		Bson bsonFilter = Filters.eq("_id", genesisBlock.getBody().getUserName().concat(AppUtils.convertDateToString(new Date())));
		Document existingBlockchain = actualCollection.find(bsonFilter).first();
		if(existingBlockchain != null) {
			MongoCollection<Document> archiveCollection = mdb.getCollection(AppProperties.getMongodbCollectionArchive());
			archiveCollection.deleteOne(bsonFilter);
			archiveCollection.insertOne(existingBlockchain);
			actualCollection.deleteOne(bsonFilter);
		}
		
		BlockchainDTO newBlockchain = new BlockchainDTO();
		newBlockchain.setId(genesisBlock.getBody().getUserName().concat(AppUtils.convertDateToString(new Date())));
		List<BlockDTO> blocks = new ArrayList<BlockDTO>();
		blocks.add(genesisBlock);
		newBlockchain.setBlocks(blocks);
		actualCollection.insertOne(Document.parse(AppUtils.convertFromObjectToJson(newBlockchain)));
		Document createdBlockchain = actualCollection.find(bsonFilter).first();
		if(createdBlockchain != null)
			createdBC = true;
		
		return createdBC;
	}
}
