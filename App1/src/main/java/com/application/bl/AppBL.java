package com.application.bl;

import java.security.Key;
import java.util.Date;
import java.util.Random;

import javax.jms.Message;

import org.apache.activemq.util.ByteSequence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.stereotype.Component;

import com.app.db.ApplicationRepository;
import com.app.properties.AppProperties;
import com.application.dto.BlockDTO;
import com.application.dto.BodyDTO;
import com.application.dto.HeaderDTO;
import com.application.dto.MessageDTO;
import com.application.model.User;
import com.application.security.SecurityUtils;
import com.google.gson.Gson;

@Component
public class AppBL {
	
	@Autowired
	SecurityUtils securityUtils;
	
	@Autowired
	AppProperties appProperties;
	
	@Autowired
	ApplicationRepository applicationRepository;
	
	@Autowired
	JmsTemplate jmsTemplate;
	
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
			Key publicKey = securityUtils.getPublicKey(appProperties.getX509CertificateFullPath());
			Key dSimmetricKey = (Key) securityUtils.decryptObject(message.getEncryptedSimmetricKey(), appProperties.getRsaAlgorithm(), publicKey);
			BlockDTO dGenesisBloc = (BlockDTO) securityUtils.decryptObject(message.getEncryptedBlock(), appProperties.getAesAlgorithm(), dSimmetricKey);
			
			// reading from application table with spring data
//			List<Application> listApplication = applicationRepository.findByAppID("App1");
//			listApplication.get(0).getCertificate().getBinaryStream();
////			InputStreamReader isr = new InputStreamReader(listApplication.get(0).getCertificate().getBinaryStream());
//			CertificateFactory certificateFactory = CertificateFactory.getInstance(appProperties.getX509Certificate());
//			X509Certificate x509certificate = (X509Certificate) certificateFactory.generateCertificate(listApplication.get(0).getCertificate().getBinaryStream());
//			publicKey = x509certificate.getPublicKey();

			Gson gson = new Gson();
			String jsonMessage = gson.toJson(message);
			jmsTemplate.convertAndSend("app1inputQueue", jsonMessage);
			
//			Message messageReceived = jmsTemplate.receive("app1outputQueue");
//			
//			messageReceived.getBody(java.io.Serializable.class);
			com.app.dto.MessageDTO received = (com.app.dto.MessageDTO) jmsTemplate.receiveAndConvert("app1outputQueue");
			received.toString();
			return true; // TODO
			
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}
}
