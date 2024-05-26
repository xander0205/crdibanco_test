package com.credibanco.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


@Configuration
@PropertySources({
	@PropertySource(value="classpath:controllers.properties"),
	@PropertySource(value="classpath:messages.properties", encoding="UTF-8")
	
})
public class AppConfig{


}
