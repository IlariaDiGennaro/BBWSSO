package com.app.utilities;

import com.google.gson.Gson;

public class Utilities {
	public static <T> Object convertFromJsonToObject(String json, Class<T> clazz) {
		Gson gson = new Gson();
		Object jsonObject = gson.fromJson(json, clazz);
		return jsonObject;
	}
}
