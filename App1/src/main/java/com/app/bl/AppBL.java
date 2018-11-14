package com.app.bl;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.app.db.ApplicationRepository;
import com.app.db.BlockchainRepository;
import com.app.db.UserRepository;
import com.app.dto.BlockDTO;
import com.app.dto.BlockchainDTO;
import com.app.dto.BodyDTO;
import com.app.dto.HeaderDTO;
import com.app.dto.MessageDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.UserDTO;
import com.app.jms.CorrelationIdPostProcessor;
import com.app.model.User;
import com.app.properties.AppProperties;
import com.app.security.SecurityUtils;
import com.app.utilities.AppUtils;

@Component
public class AppBL {
	
	@Autowired
	SecurityUtils securityUtils;
	
	@Autowired
	AppProperties appProperties;
	
//	@Autowired
//	ApplicationRepository applicationRepository;
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	BlockchainRepository blockchainRepository;
	
	@Autowired
	AppUtils appUtils;
	
	@Autowired
	UserRepository userRepository;
	
	private static Logger logger = LogManager.getLogger();
	
	private Random nonceGenerator;
	
	public boolean userLogin(User user) throws Exception {
		try {

			if(nonceGenerator == null)
				nonceGenerator = new Random();

			HeaderDTO header = new HeaderDTO();
			header.setPrevHash(null);
			header.setDataHash(securityUtils.sha256Hash(user.getUsername().concat(user.getPassword())));
			header.setTimeStamp(new Date());
			header.setNonce(nonceGenerator.nextInt());

			BodyDTO body = new BodyDTO();
			body.setUserName(user.getUsername());

			BlockDTO genesisBlock = new BlockDTO();
			genesisBlock.setHeader(header);
			genesisBlock.setBody(body);



			//String encryptedObject = securityUtils.encryptObject(genesisBlock.getBody());
			Key simmetricKey = securityUtils.getSimmetricKey();
			String encryptedBlock = securityUtils.encryptObject(genesisBlock, appProperties.getAesAlgorithm(), simmetricKey);
			// TODO decrypt object
			//BodyDTO decryptedBody = (BodyDTO) securityUtils.decryptObject(encryptedObject);

			Key privateKey = securityUtils.getPrivateKey(appProperties.getEncryptionKeyFullPath());
			String encryptedSimmetricKey = securityUtils.encryptObject(simmetricKey, appProperties.getRsaAlgorithm(), privateKey);

			MessageDTO message = new MessageDTO();
			message.setSender(appProperties.getThisAppIdentifier());
			message.setReceiver(appProperties.getOtherAppIdentifiers());
			message.setEncryptedBlock(encryptedBlock);
			message.setEncryptedSimmetricKey(encryptedSimmetricKey);

			// decryption
			//			Key publicKey = securityUtils.getPublicKey(appProperties.getX509CertificateFullPath());
			//			Key dSimmetricKey = (Key) securityUtils.decryptObject(message.getEncryptedSimmetricKey(), appProperties.getRsaAlgorithm(), publicKey);
			//			BlockDTO dGenesisBloc = (BlockDTO) securityUtils.decryptObject(message.getEncryptedBlock(), appProperties.getAesAlgorithm(), dSimmetricKey);

			// reading from application table with spring data
			//			List<Application> listApplication = applicationRepository.findByAppID("App1");
			//			listApplication.get(0).getCertificate().getBinaryStream();
			////			InputStreamReader isr = new InputStreamReader(listApplication.get(0).getCertificate().getBinaryStream());
			//			CertificateFactory certificateFactory = CertificateFactory.getInstance(appProperties.getX509Certificate());
			//			X509Certificate x509certificate = (X509Certificate) certificateFactory.generateCertificate(listApplication.get(0).getCertificate().getBinaryStream());
			//			publicKey = x509certificate.getPublicKey();



			//jmsTemplate.convertAndSend("app1inputQueue", message);
			String correlationID = UUID.randomUUID().toString();
			jmsTemplate.convertAndSend("app1inputQueue", message, new CorrelationIdPostProcessor(correlationID));
			// TODO send to all the net

			//			Message messageReceived = jmsTemplate.receive("app1outputQueue");
			//			
			//			messageReceived.getBody(java.io.Serializable.class);
			//ResponseDTO response = (ResponseDTO) jmsTemplate.receiveAndConvert("app1outputQueue");
			ResponseDTO response = (ResponseDTO) jmsTemplate.receiveSelectedAndConvert("app1outputQueue", "JMSCorrelationID='"+correlationID+"'");

			// TODO receive true from 50% +1 of the net

			if(!response.isValidBlock())
				return false;

			// TODO build new blockchain with genesisBlock
			BlockchainDTO userBlockchain = new BlockchainDTO();
			String userBlockchainID = body.getUserName().concat(appUtils.convertDateToString(new Date(), appProperties.getDateFormat()));
			userBlockchain.setId(userBlockchainID);
			List<BlockDTO> blocks = new ArrayList<BlockDTO>();
			blocks.add(genesisBlock);
			userBlockchain.setBlocks(blocks);

			if(blockchainRepository.existsById(userBlockchainID)) {
				blockchainRepository.deleteById(userBlockchainID);
			}
			blockchainRepository.save(userBlockchain);
			if(!blockchainRepository.existsById(userBlockchainID))
				return false;
			
			if(!userAccess(user.getUsername()))
				return false;

			return true;

		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}
	
	public boolean userAccess(String userCookie) throws Exception{
		if(nonceGenerator == null)
			nonceGenerator = new Random();
		
		HeaderDTO header = new HeaderDTO();
		
		header.setTimeStamp(new Date());
		header.setNonce(nonceGenerator.nextInt());
		
		BodyDTO body = new BodyDTO();
		body.setUserName(userCookie);
		body.setAppID(appProperties.getThisAppIdentifier());
		
		header.setDataHash(securityUtils.sha256Hash(body.getUserName().concat(body.getAppID())));
		
		String blockchainID = userCookie.concat(appUtils.convertDateToString(new Date(),appProperties.getDateFormat()));
		
		if(blockchainRepository.existsById(blockchainID)) {
		
			BlockchainDTO userBlockchain = blockchainRepository.findById(blockchainID).get();
			List<BlockDTO> blocks = userBlockchain.getBlocks();
			BlockDTO lastBlock = blocks.get(blocks.size()-1);
			HeaderDTO lastBlockHeader = lastBlock.getHeader();
			String prevHash = securityUtils.sha256Hash(
					lastBlockHeader.getDataHash().concat(
							lastBlockHeader.getPrevHash()!=null?lastBlockHeader.getPrevHash():"")
					.concat(String.valueOf(lastBlockHeader.getNonce()))
					.concat(appUtils.convertDateToString(lastBlockHeader.getTimeStamp(),appProperties.getDateFormatHour())));
			header.setPrevHash(prevHash);
			
		}
		
		BlockDTO appBlock = new BlockDTO();
		appBlock.setHeader(header);
		appBlock.setBody(body);
		
		
		Key simmetricKey = securityUtils.getSimmetricKey();
		String encryptedBlock = securityUtils.encryptObject(appBlock, appProperties.getAesAlgorithm(), simmetricKey);
		
		Key privateKey = securityUtils.getPrivateKey(appProperties.getEncryptionKeyFullPath());
		String encryptedSimmetricKey = securityUtils.encryptObject(simmetricKey, appProperties.getRsaAlgorithm(), privateKey);
		
		MessageDTO message = new MessageDTO();
		message.setSender(appProperties.getThisAppIdentifier());
		message.setReceiver(appProperties.getOtherAppIdentifiers());
		message.setEncryptedBlock(encryptedBlock);
		message.setEncryptedSimmetricKey(encryptedSimmetricKey);
		
		String correlationID = UUID.randomUUID().toString();
		jmsTemplate.convertAndSend("app1inputQueue", message, new CorrelationIdPostProcessor(correlationID));
		// TODO send to all the net
		
		ResponseDTO response = (ResponseDTO) jmsTemplate.receiveSelectedAndConvert("app1outputQueue", "JMSCorrelationID='"+correlationID+"'");
		// TODO receive true from 50% +1 of the net
		
		if(response.isValidBlock()) {
			// TODO
			String userBlockchainID = userCookie.concat(appUtils.convertDateToString(new Date(), appProperties.getDateFormat()));
			if(!blockchainRepository.existsById(userBlockchainID)) {
				return false;
			}
			BlockchainDTO userBlockchain = blockchainRepository.findById(userBlockchainID).get();
			boolean [] result = validBlockchainAndFoundBlock(userBlockchain, appBlock);
			boolean validBlockchain = result[0];
			boolean foundBlock = result[1];
			if(!validBlockchain)
				return false;
			if(foundBlock)
				return true;
			userBlockchain.getBlocks().add(appBlock);
			blockchainRepository.save(userBlockchain);
			return true;
			
		}
		else 
			return false;
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
					calculatedDataHash = securityUtils.sha256Hash(block.getBody().getUserName().concat(block.getBody().getAppID()));

				else {
					UserDTO user = null;
					List<UserDTO> userList = userRepository.findByUsername(block.getBody().getUserName());
					if(userList != null && !userList.isEmpty()) {
						user = userList.get(0);
					}
					if(user == null) {
						output[0] = false;
						break;
					}
					calculatedDataHash = securityUtils.sha256Hash(block.getBody().getUserName().concat(user.getPassword()));
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
							.concat(appUtils.convertDateToString(header.getTimeStamp(),appProperties.getDateFormatHour()));
					vPrevHash = securityUtils.sha256Hash(stringToHash);
				

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
