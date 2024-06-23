package com.example.firstrestapi.Database;

import com.example.firstrestapi.EnvConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
@Component
public class DBManager {
    private static final Logger log = LoggerFactory.getLogger(DBManager.class);
    EnvConfig envConfig;
    @Autowired
    public DBManager(EnvConfig envConfig) {
        this.envConfig = envConfig;
        this.connect();
    }
    private Connection connection;
    public void connect() {
        String url = this.envConfig.getConnectionString();
        String user = this.envConfig.getUserName();
        String password = this.envConfig.getUserPassword();

        log.info("Connecting to database with:");
        log.info("- string: {}" , url);
        log.info("- user: {}" , user);
        log.info("- password: {}" , password);
        try {
            this.connection = DriverManager.getConnection(url,user,password);
            log.info("Successfully connected to database");
        } catch (SQLException e){
            log.error("Unable to connect to database", e);

        }



    }
    public Connection getConnection(){
        return this.connection;
    }
}
