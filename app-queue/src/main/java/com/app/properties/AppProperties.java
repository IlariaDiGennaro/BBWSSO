package com.app.properties;

public class AppProperties {
	private static String x509Certificate;
	private static String rsaAlgorithm;
	private static String aesAlgorithm;
	private static String shaAlgorithm;
	private static String mongodbPrefix;
	private static String mongodbCollectionActual;
	
	public static String getX509Certificate() {
		return x509Certificate;
	}

	public static void setX509Certificate(String x509Certificate) {
		AppProperties.x509Certificate = x509Certificate;
	}

	public static String getRsaAlgorithm() {
		return rsaAlgorithm;
	}

	public static void setRsaAlgorithm(String rsaAlgorithm) {
		AppProperties.rsaAlgorithm = rsaAlgorithm;
	}

	public static String getAesAlgorithm() {
		return aesAlgorithm;
	}

	public static void setAesAlgorithm(String aesAlgorithm) {
		AppProperties.aesAlgorithm = aesAlgorithm;
	}

	public static String getShaAlgorithm() {
		return shaAlgorithm;
	}

	public static void setShaAlgorithm(String shaAlgorithm) {
		AppProperties.shaAlgorithm = shaAlgorithm;
	}

	public static String getMongodbPrefix() {
		return mongodbPrefix;
	}

	public static void setMongodbPrefix(String mongodbPrefix) {
		AppProperties.mongodbPrefix = mongodbPrefix;
	}

	
	
}
