package com.example.firstrestapi.service;
import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DTOs.BaseProduct;
import com.example.firstrestapi.DTOs.ProductTeaser;
import com.example.firstrestapi.util.PriceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LanguageHandler {

    public static final String fallbackLanguage = "EN";
    private static final Logger log = LoggerFactory.getLogger(LanguageHandler.class);
    private final FallbackHandler fallbackHandler;
    private static LanguageHandler instance;
    private LanguageHandler() {
        this.fallbackHandler = FallbackHandler.getInstance();
    }

    public static LanguageHandler getInstance() {
        if(Objects.isNull(instance)) {
            instance = new LanguageHandler();
        }
        return instance;
    }


    public Optional<List<ProductTeaser>> getProductsWithFullTranslation(List<BaseProduct> baseProducts, String languageId){
        List<ProductTeaser> productTeasers = baseProducts.stream().map(ProductTeaser::new).toList();

        // Adds displayName and displayPrice without fallback
        fallbackHandler.injectDisplayPriceAndDisplayNameIntoProductsWithFallback(productTeasers, languageId);

        // fallback for displayName and displayPrice


        // Add details to product without fallback
        fallbackHandler.injectTranslatedDetailsIntoProductsWithFallback(productTeasers, languageId);

        // Adds fallback details if needed


        List<ProductTeaser> productTeaserList = productTeasers
                .stream()
                .filter(Objects::nonNull)
                .toList();
        return Optional.of(productTeaserList);
    }



    public PriceHelper getPriceHelperByLanguage(String language){
        LanguageDAO languageDAO = new LanguageDAO();
        return languageDAO.getPriceInformationForLanguage(language);
    }


}
