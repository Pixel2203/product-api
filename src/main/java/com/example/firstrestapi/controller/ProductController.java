package com.example.firstrestapi.controller;

import com.example.firstrestapi.CHeaders;
import com.example.firstrestapi.DTOs.RatingContext;
import com.example.firstrestapi.DTOs.RegisterProductRequest;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.service.ProductService;
import jakarta.annotation.Nullable;
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
            @RequestHeader(CHeaders.LOCALE) String lang,
            @RequestBody Map<Integer, Integer> productIds){
        return productService.getProductsByIdsWithFallback(productIds, lang);
    }

    /**
     * Returns a Product Object for the given productId
     * @param productId ProductId of the wanted product from the database
     * @param locale Desired Language of the product
     * @return
     */
    @GetMapping("/product/{productId}")
    public EventResponse<?> getProductById(
            @PathVariable int productId,
            @RequestHeader(value = CHeaders.LOCALE,required = false, defaultValue = "de") String locale,
            @RequestHeader(value = CHeaders.USERID, required = false) @Nullable Integer userId){
        return productService.getProductById(productId, locale, userId);
    }

    @PostMapping("/rate")
    public EventResponse<?> rateProduct(
            @RequestBody RatingContext context,
            @RequestHeader(CHeaders.USERID) int uId,
            @RequestHeader(CHeaders.LOCALE) String language){
        return productService.addRatingToProduct(context, uId, language);
    }

    @DeleteMapping("/rate/{productId}")
    public EventResponse<?> deleteRating(@RequestHeader(CHeaders.USERID) int uId, @RequestBody String ratingId, @PathVariable int productId){
       return productService.removeRatingFromProduct(ratingId, uId, productId);
    }

    @PostMapping("/product")
    public EventResponse<?> addProduct(@RequestBody RegisterProductRequest request){
        return productService.registerProduct(request);
    }
}
