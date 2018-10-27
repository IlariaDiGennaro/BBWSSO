package com.application.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.properties.AppProperties;

@Component
public class SecurityUtils {
	
	private static Logger logger = LogManager.getLogger();
	
	@Autowired
	AppProperties appProperties;
	
	public String sha256Hash(String stringToHash) throws Exception {
		String hashedString = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(appProperties.getShaAlgorithm());
			byte[] hashBytes = messageDigest.digest(stringToHash.getBytes(StandardCharsets.UTF_8));
			hashedString = new String(Hex.encode(hashBytes));
			
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
			throw new Exception(e);
		}
		return hashedString;
	}
	
	private PrivateKey getPrivateKey(String fullPathFile) {
		PrivateKey privateKey = null;
		try {
			// TODO
			File privateKeyFile = new File(fullPathFile);
			PemReader pemReader = new PemReader(new FileReader(privateKeyFile));
			PemObject pemObject = pemReader.readPemObject();
			// TODO add this in pom bcprov-jdk15on, bcmail-jdk15on and bcpkix-jdk15on
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return privateKey;
	}
}
