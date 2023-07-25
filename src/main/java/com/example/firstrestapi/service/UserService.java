package com.example.firstrestapi.service;

import com.example.firstrestapi.DAOs.UserDAO;
import com.example.firstrestapi.DTOs.UserDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    public Optional<UserDTO> getUser(Integer id){
        UserDAO userAO = new UserDAO();
        return userAO.getUser(id);
    }
    public boolean addUser(UserDTO user){
        UserDAO userAO = new UserDAO();
        return userAO.addUser(user);
    }

    public Optional<List<UserDTO>> getUsers() {
        UserDAO userAO = new UserDAO();
        return userAO.getAllUsers();
    }
    public Optional<List<UserDTO>> getUsersBySearchTerm(String[] args) {
        UserDAO userAO = new UserDAO();
        return userAO.findUsers(args);
    }

    public Optional<Boolean>updateUser(UserDTO updatedUser) {
        UserDAO userAO = new UserDAO();
        return userAO.updateUser(updatedUser);
    }
}
