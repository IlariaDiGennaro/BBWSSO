package com.app.security;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.properties.AppProperties;
import com.app.utilities.AppUtils;

public class SecurityUtils {
	
	private static Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
	
	public static PublicKey getPublicKey(InputStream inputStream) throws Exception {
		PublicKey publicKey = null;
		
		CertificateFactory certificateFactory = CertificateFactory.getInstance(AppProperties.getX509Certificate());
		X509Certificate x509certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
		publicKey = x509certificate.getPublicKey();
		return publicKey;
	}
	
	public static Object decryptObject(String encryptedObject,String decryptionAlgorithm, Key decriptionKey) throws Exception {
		Object decryptedObject = null;
		try {
			Cipher decryptCipher = Cipher.getInstance(decryptionAlgorithm);
			decryptCipher.init(Cipher.DECRYPT_MODE, decriptionKey);
			byte [] encryptedBytes = Base64.getDecoder().decode(encryptedObject);
			byte [] decryptedBytes = decryptCipher.doFinal(encryptedBytes);
			decryptedObject = AppUtils.convertBytesToObject(decryptedBytes);
		} catch (NoSuchAlgorithmException e) {
			logger.error("SecurityUtils",e);
			throw new Exception(e);
		} catch (NoSuchPaddingException e) {
			logger.error("SecurityUtils",e);
			throw new Exception(e);
		} catch (InvalidKeyException e) {
			logger.error("SecurityUtils",e);
			throw new Exception(e);
		} catch (IllegalBlockSizeException e) {
			logger.error("SecurityUtils",e);
			throw new Exception(e);
		} catch (BadPaddingException e) {
			logger.error("SecurityUtils",e);
			throw new Exception(e);
		} catch (Exception e) {
			logger.error("SecurityUtils",e);
			throw new Exception(e);
		}
		
		return decryptedObject;
	}
	
	public static String sha256Hash(String stringToHash) throws Exception {
		String hashedString = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(AppProperties.getShaAlgorithm());
			byte[] hashBytes = messageDigest.digest(stringToHash.getBytes(StandardCharsets.UTF_8));
			hashedString = new String(Hex.encode(hashBytes));
			
		} catch (NoSuchAlgorithmException e) {
			logger.error("SecurityUtils",e);
			throw new Exception(e);
		}
		return hashedString;
	}
}
