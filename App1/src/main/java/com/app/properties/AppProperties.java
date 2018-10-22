package com.app.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
	
	@Value("${login.cookie}")
	private  String loginCookie;
	@Value("${day.expiration.cookie}")
	private String dayExpirationCookie;

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

	
	
}
