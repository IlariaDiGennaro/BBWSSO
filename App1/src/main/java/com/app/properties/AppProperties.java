package com.app.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
	
	@Value("${login.cookie}")
	private  String loginCookie;
	
	@Value("${day.expiration.cookie}")
	private String dayExpirationCookie;
	
	@Value("${sha.algorithm}")
	private String shaAlgorithm;
	
	@Value("${encryption.key.full.path}")
	private String encryptionKeyFullPath;
	
	@Value("${provider.name}")
	private String providerName;
	
	@Value("${rsa.algorithm}")
	private String rsaAlgorithm;

	public String getLoginCookie() {
		return loginCookie;
	}

	public void setLoginCookie(String loginCookie) {
		this.loginCookie = loginCookie;
	}

	public String getDayExpirationCookie() {
		return dayExpirationCookie;
	}

	public void setDayExpirationCookie(String dayExpirationCookie) {
		this.dayExpirationCookie = dayExpirationCookie;
	}

	public String getShaAlgorithm() {
		return shaAlgorithm;
	}

	public void setShaAlgorithm(String shaAlgorithm) {
		this.shaAlgorithm = shaAlgorithm;
	}

	public String getEncryptionKeyFullPath() {
		return encryptionKeyFullPath;
	}

	public void setEncryptionKeyFullPath(String encryptionKeyFullPath) {
		this.encryptionKeyFullPath = encryptionKeyFullPath;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getRsaAlgorithm() {
		return rsaAlgorithm;
	}

	public void setRsaAlgorithm(String rsaAlgorithm) {
		this.rsaAlgorithm = rsaAlgorithm;
	}

	
}
