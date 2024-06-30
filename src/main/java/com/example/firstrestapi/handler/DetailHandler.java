package com.example.firstrestapi.handler;

import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DTOs.ProductTeaser;

import java.util.List;
import java.util.Objects;

public class DetailHandler {
    private static DetailHandler instance;
    private DetailHandler(){}
    public static DetailHandler getInstance() {
        if(Objects.isNull(instance)) {
            instance = new DetailHandler();
        }
        return instance;
    }


    public void injectTranslatedDetailsIntoProducts(List<ProductTeaser> productTeasers, String languageId) {
        LanguageDAO languageDAO = new LanguageDAO();
        for (ProductTeaser product : productTeasers) {
            var details = languageDAO.getProductDetailsByProductIdAndLanguageId(product.getId(), languageId);
            if(details.isEmpty()) { continue; }
            details.sort((detail1, detail2) -> {
                int length1 = detail1.displayName().length() + detail1.value().length();
                int length2 = detail2.displayName().length() + detail2.value().length();
                return Integer.compare(length1, length2);
            });
            product.setDetails(details);
        }
    }





}
