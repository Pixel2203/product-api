package com.example.firstrestapi;


import com.example.firstrestapi.Database.DBManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.sql.SQLException;
import java.util.Locale;

@SpringBootApplication
public class FirstRestApiApplication {
    public static void main(String[] args){
        SpringApplication.run(FirstRestApiApplication.class, args);
    }
}
