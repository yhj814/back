package com.app.ggumteo.service.api;

import com.app.ggumteo.config.SecretConfigProperties;
import org.springframework.stereotype.Component;

@Component
public class ApiService {

    private final SecretConfigProperties secretConfigProperties;

    public ApiService(SecretConfigProperties secretConfigProperties) {
        this.secretConfigProperties = secretConfigProperties;
    }

    public void sendSms() {
        String apiKey = secretConfigProperties.getCoolSmsApiKey();
        String apiSecret = secretConfigProperties.getCoolSmsApiSecret();
        String number = secretConfigProperties.getCoolSmsNumber();

        // SMS API logic here
    }

    public void sendEmail() {
        String emailId = secretConfigProperties.getEmailUsername();
        String emailPassword = secretConfigProperties.getEmailPassword();

        // Email API logic here
    }
}
