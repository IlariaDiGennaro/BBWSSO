package com.app.bl;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.camel.Exchange;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.dao.ApplicationDAO;
import com.app.dao.BlockchainDAO;
import com.app.dao.UserDAO;
import com.app.dto.ApplicationDTO;
import com.app.dto.BlockDTO;
import com.app.dto.BlockchainDTO;
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
		MessageDTO receivedMessage = (MessageDTO) exchange.getIn().getBody();
		try {
		
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
		}catch(Exception exception) {
			logger.error("manageBlock", exception);
			responseToSend.setReceiver(receivedMessage.getSender());
			responseToSend.setSender(receivedMessage.getReceiver());
			responseToSend.setValidBlock(false);
		}
		return responseToSend;
	}
	
	public ResponseDTO manageBlockTEST(Exchange exchange) throws Exception {
		ResponseDTO responseToSend = new ResponseDTO();
		MessageDTO receivedMessage = (MessageDTO) AppUtils.convertFromJsonToObject(exchange.getIn().getBody().toString(), MessageDTO.class);
		try {
		
	
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
			responseToSend.setReceiver(receivedMessage.getSender());
			responseToSend.setSender(receivedMessage.getReceiver());
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
		
		else {
			// APPi BLOCK
			String username = block.getBody().getUserName();
			String app = block.getBody().getAppID();
			String dataHash = header.getDataHash();
			if(username == null || "".equals(username) || app == null || "".equals(app) || dataHash == null || "".equals(dataHash)) {
				String exception = "Block not valid";
				throw new Exception(exception);
			}
			String calculatedDataHash = SecurityUtils.sha256Hash(username.concat(app));
			if(!calculatedDataHash.equals(dataHash)) {
				String exception = "Block not valid";
				throw new Exception(exception);
			}
			
			BlockchainDTO userBlockchain = blockchainDAO.findById(username, receiver);
			if(userBlockchain == null) {
				String exception = "Blockchain for user "+username +" not found.";
				throw new Exception(exception);
			}
			
			boolean [] output = validBlockchainAndFoundBlock(userBlockchain,block);
			boolean validBlockchain = output[0];
			boolean foundBlock = output[1];
			
			if(!validBlockchain) {
				blockchainDAO.invalidBlockchain(username, receiver);
				String exception = "Blockchain not valid";
				throw new Exception(exception);
			}
			
			if(foundBlock) {
				validBlock = true;
			}
			
			else {
				// block not found
				
				// search user in db
				UserDTO user = userDAO.readByUsername(username);
				if(user == null) {
					String exception = "User ".concat(username).concat(" not found.");
					throw new Exception(exception);
				}
				
				boolean accessAllowed = accessAllowed(String.valueOf(application.getId()), user.getApps());
				if(!accessAllowed) {
					String exception = "User ".concat(username).concat(" cannot access Application ").concat(appID);
					throw new Exception(exception);
				}
				
				userBlockchain.getBlocks().add(block);
				blockchainDAO.updateUserBlockchain(userBlockchain, receiver);
				validBlock = true;
			}
				
				
		} 
		
		return validBlock;
	}
	
	private boolean accessAllowed(String appID, String apps) {
		boolean accessAllowed = false;
		if(apps.contains("|")) {
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
	
	private boolean [] validBlockchainAndFoundBlock(BlockchainDTO userBlockchain, BlockDTO blockToFound) throws Exception {
		boolean [] output = new boolean[2];
		output[0] = true; // valid blockchain
		output[1] = false; // found block

		try {
			String vPrevHash = null;

			List<BlockDTO> blocks = userBlockchain.getBlocks();

			if(blocks == null || blocks.isEmpty()) {
				output[0] = false;
				return output;
			}

			ListIterator<BlockDTO> iterator = blocks.listIterator();
			while(iterator.hasNext()) {
				BlockDTO block = iterator.next();
				String calculatedDataHash = null;

				if(block.getBody().getAppID() != null && !"".equals(block.getBody().getAppID()))
					calculatedDataHash = SecurityUtils.sha256Hash(block.getBody().getUserName().concat(block.getBody().getAppID()));

				else {
					UserDTO user = userDAO.readByUsername(block.getBody().getUserName());
					if(user == null) {
						output[0] = false;
						break;
					}
					calculatedDataHash = SecurityUtils.sha256Hash(block.getBody().getUserName().concat(user.getPassword()));
				}

				if(!calculatedDataHash.equals(block.getHeader().getDataHash())) {
					output[0] = false;
					break;
				}

				if(block.getHeader().getPrevHash() != null && !"".equals(block.getHeader().getPrevHash())) {
					// not genesis block
					if(vPrevHash != null && !"".equals(vPrevHash)) {
						if(!vPrevHash.equals(block.getHeader().getPrevHash())) {
							output[0] = false;
							break;
						}
					}
				}
					HeaderDTO header = block.getHeader();
					String stringToHash = header.getDataHash().concat(
							header.getPrevHash()!=null? header.getPrevHash() : "")
							.concat(String.valueOf(header.getNonce()))
							.concat(header.getTimeStamp());
					vPrevHash = SecurityUtils.sha256Hash(stringToHash);
				

				if(!output[1] && block.getBody().getUserName().equals(blockToFound.getBody().getUserName()) && 
						(block.getBody().getAppID() != null? block.getBody().getAppID() : "").equals(blockToFound.getBody().getAppID())) {
					output[1] = true;
				}


			}
		} catch (Exception e) {
			logger.error("validBlockchainAndFoundBlock", e);
			throw new Exception(e);
		}

		return output;
	}
}
