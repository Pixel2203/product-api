package com.example.firstrestapi.controller;

import com.example.firstrestapi.Records.Forms.RegisterForm;
import com.example.firstrestapi.Records.Forms.LoginForm;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.service.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
public class UserController {

    private UserService service;

    @Autowired
    public UserController(UserService service){
        this.service = service;
    }

    @PostMapping("/cart")
    public EventResponse<?> getProductsInCart(
        @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String lang,
        @RequestHeader("user") int uid,
        @RequestBody Map<Integer, Integer> productIds){
        return service.getProductsInCartByIds(productIds,lang);
    }

}
