package com.example.firstrestapi;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@Getter
public class EnvConfig {

    @Value("${database.user.password}")
    private String userPassword;

    @Value("${database.user.name}")
    private String userName;

    @Value("${database.string}")
    private String connectionString;

    @Value("${i18n.default.language}")
    private String defaultLanguage;

    @Value("${fallback.language}")
    private String fallbackLanguage;


}
