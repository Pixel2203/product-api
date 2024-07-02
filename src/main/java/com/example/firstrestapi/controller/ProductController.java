package com.example.firstrestapi.controller;

import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService service){
        this.productService = service;
    }

    /**
     * Returns ProductTeaser objects for the given category
     * @param categoryId
     * @param language
     * @return
     */
    @GetMapping ("/products/{categoryId}")
    public EventResponse<?> getProductsByCategory(@PathVariable String categoryId, @RequestParam String language){
        log.info("Got request to getProductsByCategory for categoryId={} and language={}", categoryId, language);
        return productService.getProductsByCategoryAndLanguage(categoryId, language);
    }

    @PostMapping("/cart")
    public EventResponse<?> getProductsInCart(
            @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String lang,
            @RequestBody Map<Integer, Integer> productIds){
        return productService.getProductsByIdsWithFallback(productIds, lang);
    }

    /**
     * Returns a Product Object for the given productId
     * @param productId ProductId of the wanted product from the database
     * @param language Desired Language of the product
     * @return
     */
    @GetMapping("/products/product/{productId}")
    public EventResponse<?> getProductById(@PathVariable int productId, @RequestParam String language){
        return productService.getProductById(productId,language);
    }

    @PostMapping("/rate")
    public EventResponse<?> rateProduct()
}
