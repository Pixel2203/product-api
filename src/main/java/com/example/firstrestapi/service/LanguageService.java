package com.example.firstrestapi.service;
import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DTOs.BaseProduct;
import com.example.firstrestapi.DTOs.ProductTeaser;
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

    public Optional<List<ProductTeaser>> getProductsWithFullTranslation(List<BaseProduct> baseProducts, String languageId){
        List<ProductTeaser> productTeasers = baseProducts.stream().map(ProductTeaser::new).toList();

        // Adds displayName and displayPrice without fallback
        injectDisplayPriceAndDisplayNameIntoProducts(productTeasers, languageId);

        // fallback for displayName and displayPrice
        productTeasers.forEach(fallbackService::checkAndFallbackProductNameAndPrice);

        // Add details to product without fallback
        detailService.injectTranslatedDetailsIntoProducts(productTeasers,languageId);
        // Adds fallback details if needed
        productTeasers.forEach(detailService::checkFallbackDetailTranslation);

        List<ProductTeaser> productTeaserList = productTeasers
                .stream()
                .filter(Objects::nonNull)
                .toList();
        return Optional.of(productTeaserList);
    }

    private void injectDisplayPriceAndDisplayNameIntoProducts(List<ProductTeaser> productTeasers, String languageId) {
        LanguageDAO languageDAO = new LanguageDAO();
        try {
            languageDAO.injectDisplayNameAndPrice(productTeasers,languageId);
        } catch (Exception e) {
            log.error("Unable to inject product translation for language {}", languageId);
        }
    }


}
