package com.application.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.properties.AppProperties;
import com.application.utilities.AppUtils;

@Component
public class SecurityUtils {
	
	private static Logger logger = LogManager.getLogger();
	
	@Autowired
	AppProperties appProperties;
	
	@Autowired
	AppUtils appUtils;
	
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
	
	private PrivateKey getPrivateKey(String fullPathFile) throws Exception {
		PrivateKey privateKey = null;
		try {
			// TODO
			Security.addProvider(new BouncyCastleProvider());
			File privateKeyFile = new File(fullPathFile);
			PEMParser pemParser = new PEMParser(new InputStreamReader(new FileInputStream(privateKeyFile)));
			PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
			JcaPEMKeyConverter pemConverter = new JcaPEMKeyConverter().setProvider(appProperties.getProviderName());
			privateKey = pemConverter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
		} catch (FileNotFoundException e) {
			logger.error(e);
			throw new Exception(e);
		} catch (IOException e) {
			logger.error(e);
			throw new Exception(e);
		}
		return privateKey;
	}
	
	public String encryptObject(Object object) throws Exception {
		String encryptedObject = null;
		try {
			PrivateKey privateKey = getPrivateKey(appProperties.getEncryptionKeyFullPath());
			Cipher encryptCipher = Cipher.getInstance(appProperties.getRsaAlgorithm());
			encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte [] encryptedBytes = encryptCipher.doFinal(appUtils.convertObjectToBytes(object));
			encryptedObject = Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
			throw new Exception(e);
		} catch (NoSuchPaddingException e) {
			logger.error(e);
			throw new Exception(e);
		} catch (InvalidKeyException e) {
			logger.error(e);
			throw new Exception(e);
		} catch (IllegalBlockSizeException e) {
			logger.error(e);
			throw new Exception(e);
		} catch (BadPaddingException e) {
			logger.error(e);
			throw new Exception(e);
		} catch (Exception e) {
			logger.error(e);
			throw new Exception(e);
		}
		
		return encryptedObject;
	}
	
	private PublicKey getPublicKey(String fullPathFile) throws Exception {
		PublicKey publicKey = null;
//		try {
//		File certificateFile = new File(fullPathFile);
//		FileReader fileReader = new FileReader(certificateFile);
//		PemReader pemReader = new PemReader(fileReader);
//		X509EncodedKeySpec x509certificate = new X509EncodedKeySpec(pemReader.readPemObject().getContent());
//		KeyFactory keyFactory = KeyFactory.getInstance(appProperties.getRsaAlgorithm());
//		publicKey = keyFactory.generatePublic(x509certificate);
//		} catch (FileNotFoundException e) {
//			logger.error(e);
//			throw new Exception(e);
//		} catch (IOException e) {
//			logger.error(e);
//			throw new Exception(e);
//		} catch (NoSuchAlgorithmException e) {
//			logger.error(e);
//			throw new Exception(e);
//		} catch (InvalidKeySpecException e) {
//			logger.error(e);
//			throw new Exception(e);
//		}
		
		File certificateFile = new File(fullPathFile);
		FileInputStream fis = new FileInputStream(certificateFile);
		CertificateFactory certificateFactory = CertificateFactory.getInstance(appProperties.getX509Certificate());
		X509Certificate x509certificate = (X509Certificate) certificateFactory.generateCertificate(fis);
		publicKey = x509certificate.getPublicKey();
		return publicKey;
	}
	
	public Object decryptObject(String encryptedObject) throws Exception {
		Object decryptedObject = null;
		try {
			PublicKey publicKey = getPublicKey(appProperties.getX509CertificateFullPath());
			Cipher decryptCipher = Cipher.getInstance(appProperties.getRsaAlgorithm());
			decryptCipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte [] encryptedBytes = Base64.getDecoder().decode(encryptedObject);
			byte [] decryptedBytes = decryptCipher.doFinal(encryptedBytes);
			decryptedObject = appUtils.convertBytesToObject(decryptedBytes);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
			throw new Exception(e);
		} catch (NoSuchPaddingException e) {
			logger.error(e);
			throw new Exception(e);
		} catch (InvalidKeyException e) {
			logger.error(e);
			throw new Exception(e);
		} catch (IllegalBlockSizeException e) {
			logger.error(e);
			throw new Exception(e);
		} catch (BadPaddingException e) {
			logger.error(e);
			throw new Exception(e);
		} catch (Exception e) {
			logger.error(e);
			throw new Exception(e);
		}
		
		return decryptedObject;
	}
}
