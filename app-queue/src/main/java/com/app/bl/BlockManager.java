package com.app.bl;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.List;

import org.apache.camel.Exchange;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.dao.ApplicationDAO;
import com.app.dao.BlockchainDAO;
import com.app.dao.UserDAO;
import com.app.dto.ApplicationDTO;
import com.app.dto.BlockDTO;
import com.app.dto.BodyDTO;
import com.app.dto.HeaderDTO;
import com.app.dto.MessageDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.UserDTO;
import com.app.properties.AppProperties;
import com.app.security.SecurityUtils;
import com.app.utilities.AppUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;

public class BlockManager {
	
	private static Logger logger = LoggerFactory.getLogger(BlockManager.class);
	
	private ApplicationDAO applicationDAO;
	
	public ApplicationDAO getApplicationDAO() {
		return applicationDAO;
	}

	public void setApplicationDAO(ApplicationDAO applicationDAO) {
		this.applicationDAO = applicationDAO;
	}
	
	private UserDAO userDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	private BlockchainDAO blockchainDAO;

	public BlockchainDAO getBlockchainDAO() {
		return blockchainDAO;
	}

	public void setBlockchainDAO(BlockchainDAO blockchainDAO) {
		this.blockchainDAO = blockchainDAO;
	}

	public ResponseDTO manageBlock(Exchange exchange) {
		ResponseDTO responseToSend = new ResponseDTO();
		try {
		MessageDTO receivedMessage = (MessageDTO) exchange.getIn().getBody();
		//TODO manage Block
		String sender = receivedMessage.getSender();
		// read application public key from db
		ApplicationDTO application = applicationDAO.readByAppId(sender);
		if(application==null) {
			String exception = "Application ".concat(sender).concat(" not found.");
			throw new Exception(exception);
		}
		
		PublicKey applicationPublicKey = SecurityUtils.getPublicKey(application.getCertificate().getBinaryStream());
		if(applicationPublicKey == null) {
			String exception = "Public key for Application ".concat(sender).concat(" not found.");
			throw new Exception(exception);
		}
		
		// decrypt simmetric key
		Key simmetricKey = (Key) SecurityUtils.decryptObject(receivedMessage.getEncryptedSimmetricKey(), AppProperties.getRsaAlgorithm(), applicationPublicKey);
		if(simmetricKey == null) {
			String exception = "Simmetric key for block decryption not found.";
			throw new Exception(exception);
		}
		
		// decrypt block
		BlockDTO block = (BlockDTO) SecurityUtils.decryptObject(receivedMessage.getEncryptedBlock(), AppProperties.getAesAlgorithm(), simmetricKey);
		if(block == null) {
			String exception = "Block not found.";
			throw new Exception(exception);
		}
		
		// build response
		responseToSend.setReceiver(receivedMessage.getSender());
		responseToSend.setSender(receivedMessage.getReceiver());
		}catch(Exception exception) {
			logger.error("manageBlockTEST", exception);
			responseToSend.setValidBlock(false);
		}
		return responseToSend;
	}
	
	public ResponseDTO manageBlockTEST(Exchange exchange) throws Exception {
		ResponseDTO responseToSend = new ResponseDTO();
		try {
		MessageDTO receivedMessage = (MessageDTO) AppUtils.convertFromJsonToObject(exchange.getIn().getBody().toString(), MessageDTO.class);
	
		//TODO manage Block
		String sender = receivedMessage.getSender();
		// read application public key from db
		ApplicationDTO application = applicationDAO.readByAppId(sender);
		if(application==null) {
			String exception = "Application ".concat(sender).concat(" not found.");
			throw new Exception(exception);
		}
		
		PublicKey applicationPublicKey = SecurityUtils.getPublicKey(application.getCertificate().getBinaryStream());
		if(applicationPublicKey == null) {
			String exception = "Public key for Application ".concat(sender).concat(" not found.");
			throw new Exception(exception);
		}
		
		// decrypt simmetric key
		Key simmetricKey = (Key) SecurityUtils.decryptObject(receivedMessage.getEncryptedSimmetricKey(), AppProperties.getRsaAlgorithm(), applicationPublicKey);
		if(simmetricKey == null) {
			String exception = "Simmetric key for block decryption not found.";
			throw new Exception(exception);
		}
		
		// decrypt block
		BlockDTO block = (BlockDTO) SecurityUtils.decryptObject(receivedMessage.getEncryptedBlock(), AppProperties.getAesAlgorithm(), simmetricKey);
		if(block == null) {
			String exception = "Block not found.";
			throw new Exception(exception);
		}
		
		// build response
		responseToSend.setValidBlock(blockValidation(application, block,receivedMessage.getReceiver()));
		responseToSend.setReceiver(receivedMessage.getSender());
		responseToSend.setSender(receivedMessage.getReceiver());
		// TODO responseToSend.setValidBlock(true);
		}
		catch(Exception exception) {
			logger.error("manageBlockTEST", exception);
			responseToSend.setValidBlock(false);
		}
		return responseToSend;
	}
	
	private boolean blockValidation(ApplicationDTO application, BlockDTO block, String receiver) throws Exception {
		boolean validBlock = false;
		// TODO 
		String appID = application.getAppID();
		HeaderDTO header = block.getHeader();
		BodyDTO body = block.getBody();
		if(header == null || body == null) {
			String exception = "Header and/or Body's block not valid.";
			throw new Exception(exception);
		}
		
//		if(body.getAppID() == null || !appID.equals(body.getAppID())) {
//			String exception = "Block's AppID ["+body.getAppID()+"] <> Message's sender ["+appID+"]";
//			throw new Exception(exception);
//		}
		
		if(header.getPrevHash() == null || "".equals(header.getPrevHash())) {
			// GENESIS BLOCK
			String username = body.getUserName();
			if(username == null || "".equals(username)) {
				String exception = "Invalid username in Block.";
				throw new Exception(exception);
			}
			// search user in db
			UserDTO user = userDAO.readByUsername(username);
			if(user == null) {
				String exception = "User ".concat(username).concat(" not found.");
				throw new Exception(exception);
			}
			// check if user can access application
//			boolean accessAllowed = accessAllowed(String.valueOf(application.getId()), user.getApps());
//			if(!accessAllowed) {
//				String exception = "User ".concat(username).concat(" cannot access Application ").concat(appID);
//				throw new Exception(exception);
//			}
			
			// valid hash data
			String calculatedDataHash = SecurityUtils.sha256Hash(user.getUsername().concat(user.getPassword()));
			if(!calculatedDataHash.equals(header.getDataHash())) {
				String exception = "Block not valid";
				throw new Exception(exception);
			}
			
			// check if exist a blockchain for user
			// if exists invalidate and re-create
			// TODO do blockchaindao and move mongoclient in blockchaindao
			if(blockchainDAO.createUserBlockchain(block,receiver)) {
				validBlock = true;
			}
			
		}
		return validBlock;
	}
	
	private boolean accessAllowed(String appID, String apps) {
		boolean accessAllowed = false;
		if(apps.contains("|")) {
			// TODO split
			String [] appArray = apps.split("|");
			for (String id : appArray) {
				if(id.equals(appID)) {
					accessAllowed = true;
					break;
				}
			}
		}
		else {
			accessAllowed = appID.equals(apps);
		}
		return accessAllowed;
	}
}
