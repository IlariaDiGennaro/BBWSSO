package com.app.dao;

import org.bson.Document;

import com.app.dto.BlockDTO;
import com.app.properties.AppProperties;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class BlockchainDAO {
	private MongoClient mongoClient;

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}
	
	public boolean createUserBlockchain(BlockDTO genesisBlock) {
		boolean createdBC = false;
		String mdbName = AppProperties.getMongodbPrefix().concat(genesisBlock.getBody().getAppID()); //MDBApp1
		MongoDatabase mdb = mongoClient.getDatabase(mdbName);
		MongoCollection<Document> collection = mdb.getCollection(collectionName)
		return createdBC;
	}
}
