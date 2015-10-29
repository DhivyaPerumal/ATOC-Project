package com.bpa.qaproduct.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;


//@Configuration
//@Profile(value={"dev", "local"})
//@PropertySource({"classpath:/properties/dev-app.properties"})
public class ApplicationDevConfig {

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }

}
  
