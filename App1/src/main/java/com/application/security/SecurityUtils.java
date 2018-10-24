package com.application.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.properties.AppProperties;

public class SecurityUtils {
	
	@Autowired
	AppProperties appProperties;
	
	public String sha256Hash(String stringToHash) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(appProperties.getShaAlgorithm());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
