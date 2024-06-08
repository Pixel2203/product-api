package com.example.firstrestapi.DAOs;

import com.example.firstrestapi.DTOs.UserDTO;
import com.example.firstrestapi.Records.Forms.LoginForm;
import com.example.firstrestapi.Records.Forms.RegisterForm;
import org.apache.logging.log4j.util.Strings;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.firstrestapi.FirstRestApiApplication.dbManager;

public class UserDAO {

    public Optional<UserDTO> getUserById(int id){
        String sql = "SELECT * FROM user WHERE id=%s".formatted(id);
        try{
            Statement stmt = dbManager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                String vorname = rs.getString("vorname");
                String nachname = rs.getString("nachname");
                String email = rs.getString("email");
                Date birth = rs.getDate("geburtsdatum");
                UserDTO user = new UserDTO(id,vorname,nachname,email, Strings.EMPTY ,birth);

                return Optional.of(user);
            }
        }catch (SQLException e){
            Logger.getLogger("UserDAO").log(Level.SEVERE, "Unable to locate user by id: " + id);
        }
        return Optional.empty();
    }


    public boolean addUser(RegisterForm form){
        SimpleDateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "INSERT INTO user (vorname,nachname,email,password,geburtsdatum) VALUES ('%s','%s','%s','%s','%s')".formatted(form.prename(), form.name(), form.email(), form.password(), mysqlDateFormat.format(form.birth()));
        try (Statement statement = dbManager.getConnection().createStatement()) {
            return statement.executeUpdate(sql) == 1;
            //return statement.execute(sql);
        }catch (SQLException e){
            return false;
        }
    }

    public boolean checkIfUserAlreadyExists(String email) {
        String sql = "SELECT * FROM user WHERE email='%s'".formatted(email);
        try (Statement statement = dbManager.getConnection().createStatement()) {
            return statement.executeQuery(sql).next();
        }catch (SQLException e){
            return false;
        }
    }
    public int matchLoginCredentials(LoginForm credentials) {
        String sql = "SELECT id FROM user WHERE email='%s' AND password='%s'".formatted(credentials.email(),credentials.password());
        try (Statement statement = dbManager.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                return resultSet.getInt("id");
            }
            return 0;
        }catch (SQLException e){
            return 0;
        }
    }

    public boolean addProductToCart(int uid, int pId) {
        Optional<Integer> amount = hasProductInCart(uid,pId);
        String sql;
        if(amount.isEmpty()){
            sql =  "INSERT INTO cart (uid,pid,amount) VALUES (%s,%s,1)".formatted(uid,pId);
        }else {
            sql = "UPDATE cart SET amount=%s WHERE uid=%s AND pid=%s".formatted(amount.get() + 1 , uid,pId);
        }
        try (Statement statement = dbManager.getConnection().createStatement()) {
            return statement.executeUpdate(sql) == 1;
            //return statement.execute(sql);
        }catch (SQLException e){
            Logger.getLogger("Unable to Add Product to cart!");
            return false;
        }
    }

    private Optional<Integer> hasProductInCart(int uid, int pId){
        String sql = "SELECT amount FROM cart WHERE uid=%s AND pid=%s".formatted(uid,pId);
        try(Statement statement = dbManager.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                return Optional.of(resultSet.getInt(("amount")));
            }
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
    // Pair because it has to return the product id with the corresponding amount for every product in cart
    public Optional<Map<Integer,Integer>> getProductsInCartByUserId(int uid) {
        String sql = "SELECT pid,amount FROM cart WHERE uid=%s".formatted(uid);
        Map<Integer,Integer> result = new HashMap<>();
        try(Statement statement = dbManager.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                result.put(resultSet.getInt("pid"), resultSet.getInt("amount"));
            }
            return Optional.of(result);
        } catch (SQLException e) {
            Logger.getLogger("UserDAO").log(Level.SEVERE, "Unable to find Products in cart!");
            return Optional.empty();
        }

    }

    /*
    public Optional<List<UserDTO>> getAllUsers(){

        String sql = "SELECT * FROM users";
        try{
            Statement statement = dbManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<UserDTO> userList = new ArrayList<>();
            while(resultSet.next()){
                userList.add(new UserDTO(
                        resultSet.getInt("id"),
                        resultSet.getString("vorname"),
                        resultSet.getString("nachname"),
                        resultSet.getString("email"),
                        resultSet.getDate("geburtsdatum")
                ));
            }

            return Optional.of(userList);
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

     */
}
