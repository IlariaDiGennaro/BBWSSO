package com.app.utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class AppUtils {
	
	private static Logger logger = LoggerFactory.getLogger(AppUtils.class);

	
	public static <T> Object convertFromJsonToObject(String json, Class<T> clazz) {
		Gson gson = new Gson();
		Object jsonObject = gson.fromJson(json, clazz);
		return jsonObject;
	}
	
	public static Object convertBytesToObject(byte [] bytes) throws Exception {
		Object object = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		    ObjectInputStream ois = new ObjectInputStream(bais);
		    object = ois.readObject();
		} catch (IOException e) {
			logger.error("AppUtils",e);
			throw new Exception(e);
		}
		return object;
	}
}
