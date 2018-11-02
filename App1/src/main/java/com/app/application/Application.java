package com.app.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@ComponentScan({"com.app.controller","com.app.properties","com.application.model.validator",
	"com.application.security","com.application.bl","com.application.utilities"})
//@EnableJpaRepositories(basePackageClasses= {ApplicationRepository.class})
@EnableJpaRepositories("com.app.db")
@EntityScan({"com.app.db"})
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableJms
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
