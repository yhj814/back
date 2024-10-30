package com.app.ggumteo.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
//import com.app.gradle.config.SecretConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@Getter
public class SecretConfigProperties {

    private static final Logger logger = LoggerFactory.getLogger(SecretConfigProperties.class);


    @Value("#{T(com.app.gradle.config.SecretConfig).COOLSMS_API_KEY}")
    private String coolSmsApiKey;

    @Value("#{T(com.app.gradle.config.SecretConfig).COOLSMS_API_SECRET}")
    private String coolSmsApiSecret;

    @Value("#{T(com.app.gradle.config.SecretConfig).COOLSMS_NUMBER}")
    private String coolSmsNumber;

    @Value("#{T(com.app.gradle.config.SecretConfig).EMAIL_USERNAME}")
    private String emailUsername;

    @Value("#{T(com.app.gradle.config.SecretConfig).EMAIL_PASSWORD}")
    private String emailPassword;

    @PostConstruct
    public void init() {
        logger.info("CoolSMS API Key: {}", coolSmsApiKey);
        logger.info("CoolSMS API Secret: {}", coolSmsApiSecret);
        logger.info("Email Username: {}", emailUsername);
        logger.info("Email Password: {}", emailPassword);
    }


}
