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
	
	@Value("${x509.certificate.full.path}")
	private String x509CertificateFullPath;
	
	@Value("${x509.certificate}")
	private String x509Certificate;
	
	@Value("${this.app.identifier}")
	private String thisAppIdentifier;
	
	@Value("${other.app.identifiers}")
	private String otherAppIdentifiers;

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

	public String getX509CertificateFullPath() {
		return x509CertificateFullPath;
	}

	public void setX509CertificateFullPath(String x509CertificateFullPath) {
		this.x509CertificateFullPath = x509CertificateFullPath;
	}

	public String getX509Certificate() {
		return x509Certificate;
	}

	public void setX509Certificate(String x509Certificate) {
		this.x509Certificate = x509Certificate;
	}

	public String getThisAppIdentifier() {
		return thisAppIdentifier;
	}

	public void setThisAppIdentifier(String thisAppIdentifier) {
		this.thisAppIdentifier = thisAppIdentifier;
	}

	public String getOtherAppIdentifiers() {
		return otherAppIdentifiers;
	}

	public void setOtherAppIdentifiers(String otherAppIdentifiers) {
		this.otherAppIdentifiers = otherAppIdentifiers;
	}
	
	
	
}
