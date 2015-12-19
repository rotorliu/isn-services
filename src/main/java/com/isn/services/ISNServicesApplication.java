package com.isn.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.isn.services.conf.CloopenSettings;

@SpringBootApplication
@EnableConfigurationProperties({CloopenSettings.class})  
public class ISNServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ISNServicesApplication.class, args);
    }
}
