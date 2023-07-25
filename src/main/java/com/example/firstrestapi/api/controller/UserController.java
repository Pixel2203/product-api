package com.example.firstrestapi.api.controller;

import com.example.firstrestapi.DTOs.UserDTO;
import com.example.firstrestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private UserService service;

    @Autowired
    public UserController(UserService service){
        this.service = service;
    }
    @CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
    @GetMapping("/user")
    public UserDTO getUser(@RequestParam Integer id){
        Optional user = service.getUser(id);
        if(user.isPresent()){
            return (UserDTO) user.get();
        }
        return null;
    }
    @CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
    @PostMapping("/adduser")
    public String addUser(@RequestBody UserDTO user){
        Optional founduser = service.getUser(user.getId());
        if(!founduser.isPresent()){
            service.addUser(user);
            return "User created!";

        }else{
            return "User could not be created!";
        }
    }
    @CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
    @GetMapping("/users")
    public List<UserDTO> getUsers(){
        Optional userList = service.getUsers();
        if(userList.isPresent()){
            return  (List<UserDTO>) userList.get();
        }
        return null;

    }
    @CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
    @PostMapping("/finduser")
    public List<UserDTO> getUsersBySearchTerm(@RequestBody String[] args){
        Optional userList = service.getUsersBySearchTerm(args);
        if(userList.isPresent()){
            return  (List<UserDTO>) userList.get();
        }
        return null;

    }
    @CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
    @PutMapping("/updateuser")
    public List<UserDTO> updateUser(@RequestBody UserDTO updatedUser){
        Optional userList = service.updateUser(updatedUser);
        if(userList.isPresent()){
            return  (List<UserDTO>) userList.get();
        }
        return null;

    }

}
