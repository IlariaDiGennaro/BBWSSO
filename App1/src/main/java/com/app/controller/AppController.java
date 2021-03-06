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

import com.app.bl.AppBL;
import com.app.model.User;
import com.app.model.validator.UserValidator;
import com.app.properties.AppProperties;

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
    	try {   	
    		// check if user is logged
    		Cookie[] cookies = request.getCookies();
    		boolean loginCookieFound = false;
    		String loginCookieValue = null;
    		if(cookies != null) {
    			for (Cookie cookie : cookies) {
    				if(cookie.getName().equals(appProperties.getLoginCookie())) {
    					loginCookieFound = true;
    					loginCookieValue = cookie.getValue();
    					break;
    				}
    			}
    		}
    		if(loginCookieFound) {

    			if(appBL.userAccess(loginCookieValue)) {
    				User user = new User();
    				user.setUsername(loginCookieValue);
    				model.put("user", user);
    				return "appHome";
    			}
    			else {
    				return "errorPage";
    			}
    		}
    		else {
    			//    		Cookie loginCookie = new Cookie(appProperties.getLoginCookie(), "userName");
    			//    		response.addCookie(loginCookie);
    			model.put("user", new User());
    			return "appLogin";
    		}
    	} catch (Exception e) {
    		logger.error(e);
    		return "errorPage";
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
				model.put("user", user);
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

    @RequestMapping(value="/logout")
    public String logout(@ModelAttribute("user") User user, HttpServletRequest request, HttpServletResponse response,Map<String, Object> model) {
    	try {
			appBL.userLogout(user);
			// delete loginCookie
    		Cookie[] cookies = request.getCookies();
    		if(cookies != null) {
    			for (Cookie cookie : cookies) {
    				if(cookie.getName().equals(appProperties.getLoginCookie())) {
    					cookie.setMaxAge(0); // 0 to delete cookie
    					response.addCookie(cookie);
    				}
    			}
    		}
    		model.put("user", new User());
			return "appLogin";
		} catch (Exception e) {
			logger.error(e);
			return "errorPage";
		}
    	
    }
    
}
