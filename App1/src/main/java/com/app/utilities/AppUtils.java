package com.app.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class AppUtils {
	
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
}
