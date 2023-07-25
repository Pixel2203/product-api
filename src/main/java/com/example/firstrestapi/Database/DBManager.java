package com.example.firstrestapi.Database;

import java.sql.*;

public class DBManager {
    private Connection connection;
    public void connect() {
        String url = "jdbc:mysql://localhost:3306/TestDatabase";
        String user = "root";
        String password = "Kaiser.331";
        try {
            this.connection = DriverManager.getConnection(url,user,password);
        } catch (SQLException e){
            e.printStackTrace();

        }


        System.out.println("Connection successfully established!");
    }
    public Connection getConnection(){
        return this.connection;
    }
}
