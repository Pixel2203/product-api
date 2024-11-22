package com.example.firstrestapi.handler;
import com.example.firstrestapi.dao.LanguageDAO;
import com.example.firstrestapi.dto.ProductTeaser;
import com.example.firstrestapi.Database.mysql.models.BaseProductModel;
import com.example.firstrestapi.util.PriceHelper;

import java.util.*;

public class LanguageHandler {

    public static final String fallbackLanguage = "en";
    private final FallbackHandler fallbackHandler;
    private final LanguageDAO languageDAO;
    public LanguageHandler(LanguageDAO languageDAO) {
        this.fallbackHandler = new FallbackHandler(languageDAO);
        this.languageDAO = languageDAO;
    }



    public List<ProductTeaser> getProductsWithFullTranslation(List<BaseProductModel> baseProducts, String languageId){
        List<ProductTeaser> productTeasers = baseProducts.stream().map(ProductTeaser::new).toList();


        productTeasers.forEach(productTeaser ->
                fallbackHandler.injectDisplayPriceAndDisplayNameWithFallback(productTeaser, languageId));


        productTeasers.forEach(productTeaser ->
                fallbackHandler.injectProductDetailsWithFallback(productTeaser, languageId));


        return productTeasers
                .stream()
                .filter(Objects::nonNull)
                .toList();
    }



    public PriceHelper getPriceHelperByLanguage(String language){
        return languageDAO.getPriceInformationForLanguage(language);
    }


}
