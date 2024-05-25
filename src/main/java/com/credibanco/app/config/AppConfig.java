package com.credibanco.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("com.credibanco.app.entities")
public class AppConfig {
    
}
