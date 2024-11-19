package com.example.firstrestapi.handler;

import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DTOs.ProductTeaser;
import com.mysql.cj.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import static com.example.firstrestapi.handler.LanguageHandler.fallbackLanguage;

public class FallbackHandler {
    private static final Logger log = LoggerFactory.getLogger(FallbackHandler.class);
    private final LanguageDAO languageDAO;
    private final DetailHandler detailHandler;
    public FallbackHandler(LanguageDAO languageDAO) {
        this.detailHandler = new DetailHandler(languageDAO);
        this.languageDAO = languageDAO;
    }

    public void injectDisplayPriceAndDisplayNameIntoProductsWithFallback(List<ProductTeaser> productTeasers, String languageId) {
        injectDisplayPriceAndDisplayNameIntoProducts(productTeasers, languageId);
        productTeasers.forEach(this::checkAndFallbackProductNameAndPrice);
    }
    private void injectDisplayPriceAndDisplayNameIntoProducts(List<ProductTeaser> productTeasers, String languageId) {
        try {
            languageDAO.injectPriceAndName(productTeasers,languageId);
        } catch (Exception e) {
            log.error("Unable to inject product translation for language {}", languageId);
        }
    }
    private void checkAndFallbackProductNameAndPrice(ProductTeaser productTeaser){

        //detailService.addTranslatedDetailsWithFallback(List.of(productDTO),fallbackLanguage, false);
        ProductTeaser fallbackProduct = ProductTeaser.copyFrom(productTeaser);

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
    }

    public void injectTranslatedDetailsIntoProductsWithFallback(List<ProductTeaser> productTeasers, String languageId) {
        detailHandler.injectTranslatedDetailsIntoProducts(productTeasers,languageId);
        productTeasers.forEach(this::checkFallbackDetailTranslation);
    }
    private void checkFallbackDetailTranslation(ProductTeaser product){
        if(!product.getDetails().isEmpty()){
            // Already has details - no need for fallback
            return;
        }
        // Loading Fallback
        ProductTeaser productCopy = ProductTeaser.copyFrom(product);
        detailHandler.injectTranslatedDetailsIntoProducts(List.of(productCopy),fallbackLanguage);
        product.setDetails(productCopy.getDetails());
    }
}
