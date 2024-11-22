package com.example.firstrestapi.Database;

import com.example.firstrestapi.EnvConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DBManager {
    private static final Logger log = LoggerFactory.getLogger(DBManager.class);

    private final EnvConfig envConfig;
    @Getter
    private Connection connection;

    @PostConstruct
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
            throw new RuntimeException("Unable to connect to database");
        }
    }

    @PreDestroy
    public void closeConnection() {
        if(Objects.nonNull(this.connection)){
            try {
                this.connection.close();
                log.info("Successfully closed database connection");
            } catch (SQLException e) {
                throw new RuntimeException("Unable to close connection to database");
            }
        }
    }
}
