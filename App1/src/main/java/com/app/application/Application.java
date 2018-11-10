package com.app.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@ComponentScan({"com.app.controller","com.app.properties","com.app.model.validator",
	"com.app.security","com.app.bl","com.app.utilities"})
//@EnableJpaRepositories("com.app.db")
//@EntityScan({"com.app.db"})
@EnableJms
@EnableMongoRepositories({"com.app.db"})
public class Application {
	
	@Bean
	public MessageSource messageSource() {
	    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	    //messageSource.setBasename("classpath:messages");
	    messageSource.setBasename("/WEB-INF/resources/validator");
	    messageSource.setCacheSeconds(10); //reload messages every 10 seconds
	    return messageSource;
	}

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
