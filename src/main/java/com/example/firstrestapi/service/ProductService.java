package com.example.firstrestapi.service;

import com.example.firstrestapi.DAOs.ProductDAO;
import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Records.RegisterProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private LanguageService languageService;
    @Autowired

    public ProductService(LanguageService languageService) {
        this.languageService = languageService;
    }

    public Optional<List<ProductDTO>> getProductsByCategoryAndLanguage(int categoryId, String languageId){
        Optional optional = Optional.empty();
        ProductDAO productDAO = new ProductDAO();
        Optional<List<ProductDTO>> optionalProducts = productDAO.getProductsByCategory(categoryId);
        if(!optionalProducts.isPresent()){
            return optional;
        }
        Optional<List<ProductDTO>> optionalProductsWithLanguage = languageService.addLanguageDetailsToProduct(optionalProducts.get(), languageId);
        if(optionalProductsWithLanguage.isPresent()){
            optional = optionalProductsWithLanguage;
        }
        return optional;
    }

    public Optional<String> registerProduct(RegisterProductRequest request) {
        return new ProductDAO().registerProductToDatabase(request);
        //return Optional.of(request.toString());

    }
}
