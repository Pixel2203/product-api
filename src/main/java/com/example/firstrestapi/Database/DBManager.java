package com.example.firstrestapi.Database;

import java.sql.*;

public class DBManager {
    private Connection connection;
    public void connect() {
        String url = "jdbc:mysql://localhost:3306/CustomDatabase";
        String user = "root";
        String password = "";
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
