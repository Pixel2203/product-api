package com.example.firstrestapi;


import com.example.firstrestapi.Database.DBManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class FirstRestApiApplication {
    public static final DBManager dbManager = new DBManager();
    public static void main(String[] args){
        dbManager.connect();
        SpringApplication.run(FirstRestApiApplication.class, args);
    }

}
