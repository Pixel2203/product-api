package com.example.firstrestapi.service;

import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DTOs.ProductTeaser;
import com.example.firstrestapi.Database.DBManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.firstrestapi.service.LanguageService.fallbackLanguage;
@Service
public class DetailService {

    private DBManager dbManager;
    @Autowired
    public DetailService(DBManager dbManager) {
        this.dbManager = dbManager;
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

    public void checkFallbackDetailTranslation(ProductTeaser product){
        if(!product.getDetails().isEmpty()){
            // Already has details - no need for fallback
            return;
        }
        // Loading Fallback
        ProductTeaser productCopy = ProductTeaser.copyFrom(product);
        injectTranslatedDetailsIntoProducts(List.of(productCopy),fallbackLanguage);
        product.setDetails(productCopy.getDetails());

    }



}
