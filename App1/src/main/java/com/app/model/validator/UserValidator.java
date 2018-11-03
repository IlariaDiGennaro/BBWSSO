package com.app.model.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.model.User;

@Component
public class UserValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		if(user.getUsername()==null || "".equals(user.getUsername()))
			errors.rejectValue("username", "campo.obbligatorio");
		if(user.getPassword()==null || "".equals(user.getPassword()))
			errors.rejectValue("password", "campo.obbligatorio");
	}

}
