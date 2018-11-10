package com.app.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.properties.AppProperties;

@Component
public class AppUtils {
	
	@Autowired
	AppProperties appProperties;
	
	private static Logger logger = LogManager.getLogger();
	
	public byte[] convertObjectToBytes(Object object) throws Exception {
		byte [] objectBytes = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			oos.flush();
			objectBytes = bos.toByteArray();
		} catch (IOException e) {
			logger.error(e);
			throw new Exception(e);
		}
		return objectBytes;
	}
	
	public Object convertBytesToObject(byte [] bytes) throws Exception {
		Object object = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		    ObjectInputStream ois = new ObjectInputStream(bais);
		    object = ois.readObject();
		} catch (IOException e) {
			logger.error(e);
			throw new Exception(e);
		}
		return object;
	}
	
	public String convertDateToString(Date date,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
