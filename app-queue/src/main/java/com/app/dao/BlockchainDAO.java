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
		MongoDatabase mdb = mongoClient.getDatabase(mdbName);
		MongoCollection<Document> actualCollection = mdb.getCollection(AppProperties.getMongodbCollectionActual());
		Bson bsonFilter = Filters.eq("_id", genesisBlock.getBody().getUserName().concat(AppUtils.convertDateToString(new Date(),AppProperties.getDateFormat())));
		Document existingBlockchain = actualCollection.find(bsonFilter).first();
		if(existingBlockchain != null) {
//			MongoCollection<Document> archiveCollection = mdb.getCollection(AppProperties.getMongodbCollectionArchive());
//			archiveCollection.deleteOne(bsonFilter);
//			archiveCollection.insertOne(existingBlockchain);
			actualCollection.deleteOne(bsonFilter);
		}
		
		BlockchainDTO newBlockchain = new BlockchainDTO();
		newBlockchain.setId(genesisBlock.getBody().getUserName().concat(AppUtils.convertDateToString(new Date(),AppProperties.getDateFormat())));
		List<BlockDTO> blocks = new ArrayList<BlockDTO>();
		blocks.add(genesisBlock);
		newBlockchain.setBlocks(blocks);
		actualCollection.insertOne(Document.parse(AppUtils.convertFromObjectToJson(newBlockchain)));
		Document createdBlockchain = actualCollection.find(bsonFilter).first();
		if(createdBlockchain != null)
			createdBC = true;
		
		return createdBC;
	}
	
	public BlockchainDTO findById(String username,String receiver) {
		BlockchainDTO userBlockchain = null;
		
		String mdbName = AppProperties.getMongodbPrefix().concat(receiver);
		MongoDatabase mdb = mongoClient.getDatabase(mdbName);
		MongoCollection<Document> actualCollection = mdb.getCollection(AppProperties.getMongodbCollectionActual());
		Bson bsonFilter = Filters.eq("_id", username.concat(AppUtils.convertDateToString(new Date(),AppProperties.getDateFormat())));
		Document userBlockchainDocument = actualCollection.find(bsonFilter).first();
		if(userBlockchainDocument != null) {
			userBlockchain = (BlockchainDTO) AppUtils.convertFromJsonToObject(userBlockchainDocument.toJson(),BlockchainDTO.class);
		}
		return userBlockchain;
	}
	
	public void invalidBlockchain(String username,String receiver) {
		String mdbName = AppProperties.getMongodbPrefix().concat(receiver);
		MongoDatabase mdb = mongoClient.getDatabase(mdbName);
		MongoCollection<Document> actualCollection = mdb.getCollection(AppProperties.getMongodbCollectionActual());
		Bson bsonFilter = Filters.eq("_id", username.concat(AppUtils.convertDateToString(new Date(),AppProperties.getDateFormat())));
		actualCollection.deleteOne(bsonFilter);
	}
	
	public void updateUserBlockchain(BlockchainDTO userBlockchain,String receiver) {
		String mdbName = AppProperties.getMongodbPrefix().concat(receiver);
		MongoDatabase mdb = mongoClient.getDatabase(mdbName);
		MongoCollection<Document> actualCollection = mdb.getCollection(AppProperties.getMongodbCollectionActual());
		Bson bsonFilter = Filters.eq("_id", userBlockchain.getId());
//		actualCollection.updateOne(bsonFilter, Document.parse(AppUtils.convertFromObjectToJson(userBlockchain)));
		actualCollection.updateOne(bsonFilter, new Document("$set", Document.parse(AppUtils.convertFromObjectToJson(userBlockchain))));
	}
}
