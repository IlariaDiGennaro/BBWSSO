package com.app.utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.dto.BlockDTO;
import com.app.properties.AppProperties;
import com.google.gson.Gson;

public class AppUtils {
	
	private static Logger logger = LoggerFactory.getLogger(AppUtils.class);

	
	public static <T> Object convertFromJsonToObject(String json, Class<T> clazz) {
		Gson gson = new Gson();
		Object jsonObject = gson.fromJson(json, clazz);
		return jsonObject;
	}
	
	public static String convertFromObjectToJson(Object jsonObject) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(jsonObject);
		return jsonString;
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
	
	public static String convertDateToString(Date date,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
