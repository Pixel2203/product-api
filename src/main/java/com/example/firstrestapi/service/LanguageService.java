package com.example.firstrestapi.service;
import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Records.*;
import com.example.firstrestapi.util.PriceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LanguageService {

    public static final String fallbackLanguage = "EN";
    private static final Logger log = LoggerFactory.getLogger(LanguageService.class);
    private final DetailService detailService;
    private final FallbackService fallbackService;

    @Autowired
    public LanguageService(DetailService detailService, FallbackService fallbackService) {
        this.detailService = detailService;
        this.fallbackService = fallbackService;
    }

    public Optional<List<ProductDTO>> getProductsWithFullTranslation(List<ProductDTO> productDTOS, String languageId){
        // Adds displayName and displayPrice without fallback
        injectDisplayPriceAndDisplayNameIntoProducts(productDTOS, languageId);

        // fallback for displayName and displayPrice
        productDTOS.forEach(fallbackService::checkAndFallbackProductNameAndPrice);

        // Add details to product without fallback
        detailService.injectTranslatedDetailsIntoProducts(productDTOS,languageId);
        // Adds fallback details if needed
        productDTOS.forEach(detailService::checkFallbackDetailTranslation);

        List<ProductDTO> productDTOList = productDTOS
                .stream()
                .filter(Objects::nonNull)
                .toList();
        return Optional.of(productDTOList);
    }

    private void injectDisplayPriceAndDisplayNameIntoProducts(List<ProductDTO> productDTOS, String languageId) {
        LanguageDAO languageDAO = new LanguageDAO();
        try {
            languageDAO.injectDisplayNameAndPrice(productDTOS,languageId);
        } catch (Exception e) {
            log.error("Unable to inject product translation for language {}", languageId);
        }
    }


}
