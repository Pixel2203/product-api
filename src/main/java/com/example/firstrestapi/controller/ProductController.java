package com.example.firstrestapi.controller;

import com.example.firstrestapi.Records.RegisterProductRequest;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService service){
        this.productService = service;
    }

    @GetMapping ("/products/{categoryId}")
    public EventResponse<?> getProductsByCategory(@PathVariable String categoryId, @RequestParam String language){
        return productService.getProductsByCategoryAndLanguage(categoryId, language);
    }
    @PostMapping ("/products")
    public EventResponse<?> getProductDetailsByIds(@RequestBody List<Integer> productIds){
        return productService.getDetailsForProductsByIds(productIds);
    }


    @PostMapping("/product")
    public String registerProduct(@RequestBody RegisterProductRequest request){
        if(request.categoryId() == 9999){
            return "Invalid Category!";
        }
        if(request.translations().isEmpty()){
            return "Could not find any translations! Must provide at least one!";
        }
        Optional<String> messageOpt = productService.registerProduct(request);
        return messageOpt.orElse(null);
    }

    @PostMapping("/cart")
    public EventResponse<?> getProductsInCart(
            @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String lang,
            @RequestHeader("user") int uid,
            @RequestBody Map<Integer, Integer> productIds){
        return productService.getProductsInCartByIds(productIds,lang);
    }
}
