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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService {
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

    public EventResponse<?> getUser(String token) {
        DecodedJWT decodedJWT = (DecodedJWT) verifyJwt(token).result();
        if(Objects.isNull(decodedJWT)){
            return EventResponse.failed("Unable to read Authentication Token!");
        }
        Optional<UserDTO> foundUser = agent.getUserById(decodedJWT.getClaim("sub").as(Integer.class));
        if(foundUser.isEmpty()){
            return EventResponse.failed("Unable to find user!");
        }
        return new EventResponse<>(true, "found user", foundUser.get());
    }

    public EventResponse<?> addToCart(String token, int pId) {
        DecodedJWT jwt = (DecodedJWT) verifyJwt(token).result();
        if(Objects.isNull(jwt)){
            return EventResponse.failed("Unable to verify token!");
        }
        int uid = Integer.parseInt(jwt.getSubject());
        boolean worked = agent.addProductToCart(uid, pId);
        return EventResponse.withoutResult(worked, worked? "Added Product To Cart" : "Unable to add Product");
    }

    public EventResponse<?> getProductsInCart(String token, String lang) {
        DecodedJWT jwt = (DecodedJWT) verifyJwt(token).result();
        if(Objects.isNull(jwt)){
            return EventResponse.failed("Unable to verify Token!");
        }
        int uid = Integer.parseInt(jwt.getSubject());
        Optional<Map<Integer,Integer>> productIdsInCart = agent.getProductsInCartByUserId(uid);
        if(productIdsInCart.isEmpty()){
            return EventResponse.failed("Unable to find products in cart!");
        }
        Optional<List<CartProductDTO>> products = productService.getProductsByIds(productIdsInCart.get(), lang);
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
