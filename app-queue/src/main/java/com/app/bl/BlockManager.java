package com.app.bl;

import java.util.Iterator;
import java.util.List;

import org.apache.camel.Exchange;
import org.bson.Document;

import com.app.dao.ApplicationDAO;
import com.app.dto.ApplicationDTO;
import com.app.dto.MessageDTO;
import com.app.dto.ResponseDTO;
import com.app.utilities.Utilities;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;

public class BlockManager {
	
	private ApplicationDAO applicationDAO;
	
	public ApplicationDAO getApplicationDAO() {
		return applicationDAO;
	}

	public void setApplicationDAO(ApplicationDAO applicationDAO) {
		this.applicationDAO = applicationDAO;
	}
	
	private MongoClient mongoClient;
	
	

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public ResponseDTO manageBlock(Exchange exchange) {
		ResponseDTO response = new ResponseDTO();
		MessageDTO message = (MessageDTO) exchange.getIn().getBody();
		//TODO manage Block
		
		// build response
		response.setReceiver(message.getSender());
		response.setSender(message.getReceiver());
		response.setValidBlock(true);
		return response;
	}
	
	public ResponseDTO manageBlockTEST(Exchange exchange) {
		ResponseDTO responseToSend = new ResponseDTO();
		MessageDTO receivedMessage = (MessageDTO) Utilities.convertFromJsonToObject(exchange.getIn().getBody().toString(), MessageDTO.class);
	
		//TODO manage Block
		String sender = receivedMessage.getSender();
		// TODO read from db public key
		ApplicationDTO application = applicationDAO.findByAppId(sender);
		FindIterable<Document> documents = mongoClient.getDatabase("provaMongo").getCollection("provaCollection").find();
		Iterator it = documents.iterator(); 
	    
	      while (it.hasNext()) {  
	         Document d = (Document) it.next();
	         d.getString("sender");
	      }
		// build response
		responseToSend.setReceiver(receivedMessage.getSender());
		responseToSend.setSender(receivedMessage.getReceiver());
		// TODO responseToSend.setValidBlock(true);
		return responseToSend;
	}
}
