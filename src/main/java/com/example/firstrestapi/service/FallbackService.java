package com.example.firstrestapi.service;

import com.example.firstrestapi.DTOs.ProductDTO;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.firstrestapi.service.LanguageService.fallbackLanguage;

@Service
public class FallbackService {
    private LanguageService languageService;
    private DetailService detailService;
    @Autowired
    public FallbackService(LanguageService languageService, DetailService detailService) {
        this.languageService = languageService;
        this.detailService = detailService;
    }

    public ProductDTO fallbackProductNameAndPrice(ProductDTO productDTO){

        detailService.addTranslatedDetailsWithFallback(List.of(productDTO),fallbackLanguage, false);
        if(StringUtils.isNullOrEmpty(productDTO.getDisplayName())){
            productDTO.setDisplayName(fallbackProduct.getDisplayName());
        }
        if(StringUtils.isNullOrEmpty(productDTO.getDisplayPrice())){
            productDTO.setDisplayPrice(fallbackProduct.getDisplayPrice());
        }
        return productDTO;
    }
}
