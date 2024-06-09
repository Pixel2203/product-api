package com.example.firstrestapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.firstrestapi.DAOs.UserDAO;
import com.example.firstrestapi.DTOs.CartProductDTO;
import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.DTOs.UserDTO;
import com.example.firstrestapi.JWT.Token;
import com.example.firstrestapi.Records.Forms.LoginForm;
import com.example.firstrestapi.Records.Forms.RegisterForm;
import com.example.firstrestapi.responses.EventResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
public class UserService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserDAO agent;
    private final ProductService productService;
    @Autowired
    public UserService(ProductService productService){
        this.productService = productService;
        this.agent = new UserDAO();
    }
    /*
    public Optional<UserDTO> getUser(int id){
        return agent.getUser(id);
    }

     */
    public EventResponse<?> addUser(RegisterForm form){
        if(!agent.checkIfUserAlreadyExists(form.email())){
            boolean worked = agent.addUser(form);
            String message = worked ?  "User successfully created!" : "Unable to register user!";
            return EventResponse.withoutResult(worked,message);
        }
        return EventResponse.withoutResult(false, "A user with this email adress already exists!");

    }

    public EventResponse<?> loginUser(LoginForm login) {
        int uId = agent.matchLoginCredentials(login);
        if(uId==0){
            log.info("Unable to find user {}" , login.email());
            return EventResponse.withoutResult(false, "Unable to find user!");
        }
        Date now = new Date();
        now.setTime(now.getTime() + (1000 * 60 * 60));
        String token = new Token(login.email(), uId, now).buildToken();
        Logger.getLogger("UserService").log(Level.INFO, "Sent cookie , expiring on " + now.toString());
        return new EventResponse<>(true, "User found!",token);
    }

    public EventResponse<?> verifyJwt(String token)   {
        try{
            Algorithm algorithm = Algorithm.HMAC256("MYKEY");
            JWTVerifier verifier = JWT.require(algorithm).build();
            try{
                DecodedJWT jwt = verifier.verify(token);
                return new EventResponse<>(true, "Good Token :)", jwt);
            }catch (TokenExpiredException e){
                Logger.getLogger("UserService").log(Level.WARNING, "Token's been expired!");
                return EventResponse.failed("Token has been expired!");
            }

        }catch (UnsupportedEncodingException e){
            Logger.getLogger("UserService").log(Level.ALL, "Unable to parse JWT Token!");
            return EventResponse.failed("Unable to parse JWT Token, Invalid Token!");
        }

    }

    public EventResponse<?> getUser(int uid) {
        Optional<UserDTO> foundUser = agent.getUserById(uid);
        if(foundUser.isEmpty()){
            return EventResponse.failed("Unable to find user!");
        }
        return new EventResponse<>(true, "found user", foundUser.get());
    }

    public EventResponse<?> addToCart(int productId, int userId) {
        boolean worked = agent.addProductToCart(userId, productId);
        return EventResponse.withoutResult(worked, worked? "Added Product To Cart" : "Unable to add Product");
    }

    public EventResponse<?> getProductsInCart(String language, int uid ) {
        Optional<Map<Integer,Integer>> productIdsInCart = agent.getProductsInCartByUserId(uid);
        if(productIdsInCart.isEmpty()){
            return EventResponse.failed("Unable to find products in cart!");
        }
        Optional<List<CartProductDTO>> products = productService.getProductsByIds(productIdsInCart.get(), language);
        if(products.isEmpty()){
            return EventResponse.failed("Unable to find products in your cart");
        }
        return new EventResponse<>(true,"Found Products!", products.get());

    }
    /*

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

     */
}
