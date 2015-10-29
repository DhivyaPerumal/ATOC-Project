package com.bpa.qaproduct.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;


//@Configuration
//@Profile(value="staging")
//@PropertySource({"classpath:/properties/staging-app.properties"})
public class ApplicationStagingConfig {

        @Autowired
        Environment environment;

      @Bean
        public TextEncryptor textEncryptor() {
            return Encryptors.queryableText(environment.getProperty("security.encryptPassword"),
                    environment.getProperty("security.encryptSalt"));
        }
} 
