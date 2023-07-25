package com.example.firstrestapi.DAOs;

import com.example.firstrestapi.DTOs.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.firstrestapi.FirstRestApiApplication.dbManager;

public class UserDAO {

    public Optional<UserDTO> getUser(int id){
        Optional opt = Optional.empty();
        String sql = "SELECT * FROM users WHERE id=" + id;
        try{
            Statement stmt = dbManager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                String vorname = rs.getString("vorname");
                String nachname = rs.getString("nachname");
                String email = rs.getString("email");
                UserDTO user = new UserDTO(id,vorname,nachname,email);

                opt = Optional.of(user);
                System.out.println("Got User data!");
            }
        }catch (SQLException e){}
        return opt;
    }
    public boolean addUser(UserDTO user){
        String sql =
                "INSERT INTO users (vorname, nachname , email) VALUES (" +
                        "'" + user.getVorname() + "'," +
                        "'" + user.getNachname() + "'," +
                        "'" + user.getEmail() + "')";
        try{
            Statement statement = dbManager.getConnection().createStatement();
            return statement.execute(sql);
        }catch (SQLException e){
            return false;
        }

    }
    public Optional<List<UserDTO>> getAllUsers(){
        String sql = "SELECT * FROM users";
        Optional optional = Optional.empty();
        try{
            Statement statement = dbManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<UserDTO> userList = new ArrayList<>();
            while(resultSet.next()){
                userList.add(new UserDTO(
                        resultSet.getInt("id"),
                        resultSet.getString("vorname"),
                        resultSet.getString("nachname"),
                        resultSet.getString("email")
                ));
            }

            optional = Optional.of(userList);
        }catch (SQLException e){

        }
        return optional;
    }
    public Optional<List<UserDTO>> findUsers(String[] args){
        Optional optional = Optional.empty();
        if(args.length == 0){
            return optional;
        }
        String searchKey = args[0];
        List<UserDTO> foundUsers = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM USERS WHERE ");
        stringBuilder.append("VORNAME LIKE'");
        stringBuilder.append(searchKey + "%'");
        stringBuilder.append(" OR NACHNAME LIKE'");
        stringBuilder.append(searchKey + "%'");
        stringBuilder.append(" OR EMAIL LIKE '");
        stringBuilder.append(searchKey + "%' ");
        try{
            Statement statement = dbManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
            if(args.length < 2){
                while(resultSet.next()){
                    foundUsers.add(
                            new UserDTO(
                                    resultSet.getInt("id"),
                                    resultSet.getString("vorname"),
                                    resultSet.getString("nachname"),
                                    resultSet.getString("email")
                            )
                    );
                }
            }else{
                while (resultSet.next()){
                    boolean addUser = true;
                    for(int i = 1; i < args.length; i++){
                        if(
                                !resultSet.getString("vorname").toLowerCase().startsWith(args[i].toLowerCase())
                                        && !resultSet.getString("nachname").toLowerCase().startsWith(args[i].toLowerCase())
                                            && !resultSet.getString("email").toLowerCase().startsWith(args[i].toLowerCase()))
                        {
                            addUser = false;
                        }
                    }
                    if(addUser){
                        foundUsers.add(
                                new UserDTO(
                                        resultSet.getInt("id"),
                                        resultSet.getString("vorname"),
                                        resultSet.getString("nachname"),
                                        resultSet.getString("email")
                                )
                        );
                    }
                }
            }
            optional = Optional.of(foundUsers);
        }catch (SQLException e){}
        return optional;
    }

    public Optional<Boolean> updateUser(UserDTO updatedUser) {
        Optional optional = Optional.empty();
        try{
            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE users ");
            builder.append("SET vorname='");
            builder.append(updatedUser.getVorname() + "', ");
            builder.append("nachname='");
            builder.append(updatedUser.getNachname() + "', ");
            builder.append("email='");
            builder.append(updatedUser.getEmail() + "' ");
            builder.append("WHERE id=");
            builder.append(updatedUser.getId());
            Statement statement = dbManager.getConnection().createStatement();
            boolean worked = statement.execute(builder.toString());
            optional = Optional.of(worked);
        }catch (SQLException e){}
        return optional;
    }
}
