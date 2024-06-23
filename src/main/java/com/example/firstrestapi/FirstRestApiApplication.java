package com.example.firstrestapi;


import com.example.firstrestapi.Database.DBManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class FirstRestApiApplication {
    public static void main(String[] args){
        SpringApplication.run(FirstRestApiApplication.class, args);
    }

}
