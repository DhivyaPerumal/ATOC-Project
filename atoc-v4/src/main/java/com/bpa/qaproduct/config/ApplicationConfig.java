package com.bpa.qaproduct.config; 

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.bpa.qaproduct.service.UserTokenService;

@Configuration
@ComponentScan(value="com.bpa.qaproduct")
public class ApplicationConfig {

	@Bean
    public UserTokenService userTokenservice(){
        return new UserTokenService();
    }
    
    
}
