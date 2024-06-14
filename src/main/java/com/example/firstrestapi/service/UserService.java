package com.example.firstrestapi.service;


import com.example.firstrestapi.DTOs.CartProductDTO;
import com.example.firstrestapi.responses.EventResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserService.class);

    private final ProductService productService;
    @Autowired
    public UserService(ProductService productService){
        this.productService = productService;

    }


    public EventResponse<?> getProductsInCartByIds(Map<Integer,Integer> productIds, String language) {
        Optional<List<CartProductDTO>> products = productService.getProductsByIds(productIds, language);
        if(products.isEmpty()){
            return EventResponse.failed("Unable to find products in your cart");
        }
        return new EventResponse<>(true,"Found Products!", products.get());
    }
}
