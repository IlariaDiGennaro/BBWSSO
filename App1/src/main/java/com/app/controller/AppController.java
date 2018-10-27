package com.app.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.properties.AppProperties;
import com.application.bl.AppBL;
import com.application.model.User;
import com.application.model.validator.UserValidator;

@Controller
public class AppController {
	
	private static Logger logger = LogManager.getLogger();
	
	@Autowired
	AppProperties appProperties;
	
	@Autowired
	UserValidator userValidator;
	
	@InitBinder("user")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(userValidator);
	}
	
	@Autowired
	AppBL appBL;
	

    @RequestMapping(value="/home")
    public String home(HttpServletRequest request, HttpServletResponse response,Map<String, Object> model) {
            	
    	// check if user is logged
    	Cookie[] cookies = request.getCookies();
    	boolean loginCookieFound = false;
    	if(cookies != null) {
    		for (Cookie cookie : cookies) {
				if(cookie.getName().equals(appProperties.getLoginCookie())) {
					loginCookieFound = true;
					break;
				}
			}
    	}
    	if(loginCookieFound)
    		return "appHome";
    	else {
//    		Cookie loginCookie = new Cookie(appProperties.getLoginCookie(), "userName");
//    		response.addCookie(loginCookie);
    		model.put("user", new User());
    		return "appLogin";
    	}
    }
    
    @SuppressWarnings("static-access")
	@RequestMapping(value="/login")
    public String login(@Valid @ModelAttribute("user") User user, Errors result, HttpServletRequest request, HttpServletResponse response,Map<String, Object> model) {
    	try {
    	if(result.hasErrors()) {
    		model.put("user", user);
    		return "appLogin";
    	}
    	
    	// TODO send genesis block to APPi
    	
			if(appBL.userLogin(user)) {

				Cookie loginCookie = new Cookie(appProperties.getLoginCookie(), user.getUsername());
				Calendar now = GregorianCalendar.getInstance();
				now.setTime(new Date());
				Calendar expirationDate = GregorianCalendar.getInstance();
				expirationDate.setTime(new Date());
				expirationDate.add(now.DAY_OF_MONTH, Integer.parseInt(appProperties.getDayExpirationCookie()));
				long start = now.getTimeInMillis();
				long end = expirationDate.getTimeInMillis();
				loginCookie.setMaxAge((int) ((end-start)/1000));
				response.addCookie(loginCookie);
				return "appHome";
			}
			else
				return "appLogin";
		} catch (NumberFormatException e) {
			logger.error(e);
			return "errorPage";
		} catch (Exception e) {
			logger.error(e);
			return "errorPage";
		}
    	
    	
    }

}
