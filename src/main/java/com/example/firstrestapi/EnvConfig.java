package com.example.firstrestapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config.properties")
public class EnvConfig {

    @Value("${database.user.password}")
    private String userPassword;

    @Value("${database.user.name}")
    private String userName;

    @Value("${database.string}")
    private String connectionString;

    public String getConnectionString() {
        return connectionString;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

}
