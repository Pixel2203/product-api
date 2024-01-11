package com.example.firstrestapi.api.controller;

import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Records.GetProductsByCategoryAndLanguageRequest;
import com.example.firstrestapi.Records.ProductDetail;
import com.example.firstrestapi.Records.RegisterProductRequest;
import com.example.firstrestapi.service.ProductService;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {
    private ProductService service;
    @Autowired
    public ProductController(ProductService service){
        this.service = service;
    }

    @PostMapping ("/products")
    @CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
    public List<ProductDTO> getProductsByCategory(@RequestBody GetProductsByCategoryAndLanguageRequest request){
        if(request.categoryId() == 9999 || StringUtils.isNullOrEmpty(request.languageId())){
            return List.of();
        }
        Optional<List<ProductDTO>> result = service.getProductsByCategoryAndLanguage(request.categoryId(), request.languageId());
        if(result.isPresent()){
            System.out.println("GEFUNDEN!");
            return result.get();
        }else{
            return List.of();
        }
    }
    @PostMapping("/product")
    @CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
    public String registerProduct(@RequestBody RegisterProductRequest request){
        if(request.categoryId() == 9999){
            return "Invalid Category!";
        }
        if(request.translations().size() == 0){
            return "Could not find any translations! Must provide at least one!";
        }
        Optional<String> messageOpt = service.registerProduct(request);
        if(messageOpt.isPresent()){
            return messageOpt.get();
        }
        return null;
    }
}
