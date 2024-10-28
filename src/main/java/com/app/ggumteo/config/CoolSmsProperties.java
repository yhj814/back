package com.app.ggumteo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@ConfigurationProperties(prefix = "coolsms.api")
public class CoolSmsProperties {
    private String key;
    private String secret;
    private String number;

    // getter 및 setter 추가
}