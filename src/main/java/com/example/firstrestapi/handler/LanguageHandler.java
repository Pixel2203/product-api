package com.example.firstrestapi.handler;
import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DTOs.BaseProduct;
import com.example.firstrestapi.DTOs.ProductTeaser;
import com.example.firstrestapi.util.PriceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LanguageHandler {

    public static final String fallbackLanguage = "EN";
    private final FallbackHandler fallbackHandler;
    private final LanguageDAO languageDAO;
    public LanguageHandler(LanguageDAO languageDAO) {
        this.fallbackHandler = new FallbackHandler(languageDAO);
        this.languageDAO = languageDAO;
    }



    public Optional<List<ProductTeaser>> getProductsWithFullTranslation(List<BaseProduct> baseProducts, String languageId){
        List<ProductTeaser> productTeasers = baseProducts.stream().map(ProductTeaser::new).toList();


        fallbackHandler.injectDisplayPriceAndDisplayNameIntoProductsWithFallback(productTeasers, languageId);
        fallbackHandler.injectTranslatedDetailsIntoProductsWithFallback(productTeasers, languageId);


        List<ProductTeaser> productTeaserList = productTeasers
                .stream()
                .filter(Objects::nonNull)
                .toList();
        return Optional.of(productTeaserList);
    }



    public PriceHelper getPriceHelperByLanguage(String language){
        return languageDAO.getPriceInformationForLanguage(language);
    }


}
