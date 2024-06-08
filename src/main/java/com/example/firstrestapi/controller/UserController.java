package com.example.firstrestapi.controller;

import com.auth0.jwt.JWT;
import com.example.firstrestapi.DTOs.UserDTO;
import com.example.firstrestapi.Records.Forms.RegisterForm;
import com.example.firstrestapi.Records.Forms.LoginForm;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
public class UserController {

    private UserService service;

    @Autowired
    public UserController(UserService service){
        this.service = service;
    }

    @GetMapping("/user")
    public EventResponse<?> getUser(@CookieValue("jwt") String token){
        return service.getUser(token);
    }



    @PostMapping("/register")
    public EventResponse<?> registerUser(@RequestBody RegisterForm form){
        return service.addUser(form);
    }



    @PostMapping("/login")
    public EventResponse<?> login(@RequestBody LoginForm login){
        return service.loginUser(login);
    }

    @PostMapping(value = "/add_to_cart")
    public EventResponse<?> addProductToShoppingCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody int pId) {
        return service.addToCart(token, pId);
    }
    @GetMapping("/cart")
    public EventResponse<?> getProductsInCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String language){
        return service.getProductsInCart(token, language);
    }
    @GetMapping("/verify")
    public EventResponse<?> verify(@CookieValue("jwt") String token) {
        return service.verifyJwt(token);
    }
    /*
    @CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
    @GetMapping("/users")
    public List<UserDTO> getUsers(){
        Optional<List<UserDTO>> userList = service.getUsers();
        return userList.orElse(null);

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

     */

}