package com.example.firstrestapi.service;

import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DTOs.ProductTeaser;
import com.mysql.cj.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.firstrestapi.service.LanguageService.fallbackLanguage;

@Service
public class FallbackService {
    private static final Logger log = LoggerFactory.getLogger(FallbackService.class);

    public void checkAndFallbackProductNameAndPrice(ProductTeaser productTeaser){

        //detailService.addTranslatedDetailsWithFallback(List.of(productDTO),fallbackLanguage, false);
        ProductTeaser fallbackProduct = ProductTeaser.copyFrom(productTeaser);
        LanguageDAO languageDAO = new LanguageDAO();

        try {
            languageDAO.injectPriceAndName(List.of(fallbackProduct), fallbackLanguage);

        } catch (Exception e) {
            log.error("Unable to inject fallback product name and price", e);
        }
        if(StringUtils.isNullOrEmpty(productTeaser.getDisplayName())){
            productTeaser.setDisplayName(fallbackProduct.getDisplayName());
        }
        if(StringUtils.isNullOrEmpty(productTeaser.getDisplayPrice())){
            productTeaser.setDisplayPrice(fallbackProduct.getDisplayPrice());
        }
        if(productTeaser.getPrice() == 0){
            productTeaser.setPrice(fallbackProduct.getPrice());
            productTeaser.setLanguageModel(fallbackLanguage);
        }
    }
}
