package com.example.firstrestapi.controller;

import com.example.firstrestapi.dto.RegisterProductRequest;
import com.example.firstrestapi.permission.Permissions;
import com.example.firstrestapi.permission.RequiresPermission;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public EventResponse<?> getProductTeasersByCategory(@PathVariable String categoryId){
        return productService.getProductsByCategoryAndLanguage(categoryId);
    }

    @PostMapping("/cart")
    public EventResponse<?> getCartProducts(
            @RequestBody Map<Integer, Integer> productIds){
        return productService.getProductsByIdsWithFallback(productIds);
    }

    /**
     * Returns a Product Object for the given productId
     * @param productId ProductId of the wanted product from the database
     * @param locale Desired Language of the product
     */
    @GetMapping("/product/{productId}")
    public EventResponse<?> getProductById(
            @PathVariable int productId){
        return productService.getProductById(productId);
    }



    @PostMapping("/product")
    @RequiresPermission(requiredPermission = Permissions.ADD_PRODUCT)
    public EventResponse<?> addProduct(@RequestBody RegisterProductRequest request){
        return productService.addProduct(request);
    }
}
