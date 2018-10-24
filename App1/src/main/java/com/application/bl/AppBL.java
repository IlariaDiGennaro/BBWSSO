package com.application.bl;

import com.application.dto.HeaderDTO;
import com.application.model.User;

public class AppBL {
	public boolean userLogin(User user) {
		HeaderDTO header = new HeaderDTO();
		header.setPrevHash(null);
		
		return false;
	}
}
