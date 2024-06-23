package com.example.firstrestapi.service;

import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.util.PriceHelper;
import com.mysql.cj.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.firstrestapi.service.LanguageService.fallbackLanguage;

@Service
public class FallbackService {
    private static final Logger log = LoggerFactory.getLogger(FallbackService.class);

    public void checkAndFallbackProductNameAndPrice(ProductDTO productDTO){

        //detailService.addTranslatedDetailsWithFallback(List.of(productDTO),fallbackLanguage, false);
        ProductDTO fallbackProduct = ProductDTO.copyFrom(productDTO);
        LanguageDAO languageDAO = new LanguageDAO();

        try {
            languageDAO.injectDisplayNameAndPrice(List.of(fallbackProduct), fallbackLanguage);

        } catch (Exception e) {
            log.error("Unable to inject fallback product name and price", e);
        }
        if(StringUtils.isNullOrEmpty(productDTO.getDisplayName())){
            productDTO.setDisplayName(fallbackProduct.getDisplayName());
        }
        if(StringUtils.isNullOrEmpty(productDTO.getDisplayPrice())){
            productDTO.setDisplayPrice(fallbackProduct.getDisplayPrice());
        }
    }
}
