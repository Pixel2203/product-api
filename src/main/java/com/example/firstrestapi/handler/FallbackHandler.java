package com.example.firstrestapi.handler;

import com.example.firstrestapi.dao.LanguageDAO;
import com.example.firstrestapi.dto.ProductTeaser;
import com.mysql.cj.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.example.firstrestapi.handler.LanguageHandler.fallbackLanguage;

@RequiredArgsConstructor
public class FallbackHandler {
    private static final Logger log = LoggerFactory.getLogger(FallbackHandler.class);
    private final LanguageDAO languageDAO;
    private final DetailHandler detailHandler = new DetailHandler();


    public void injectDisplayPriceAndDisplayNameWithFallback(ProductTeaser productTeaser, String languageId) {
        languageDAO.injectDisplayPrice(productTeaser,languageId);
        languageDAO.injectDisplayName(productTeaser,languageId);

        ProductTeaser fallbackProduct = ProductTeaser.copyFrom(productTeaser);


        languageDAO.injectDisplayPrice(fallbackProduct, fallbackLanguage);
        languageDAO.injectDisplayName(fallbackProduct, fallbackLanguage);


        if(StringUtils.isNullOrEmpty(productTeaser.getDisplayName())){
            productTeaser.setDisplayName(fallbackProduct.getDisplayName());
        }
        if(StringUtils.isNullOrEmpty(productTeaser.getDisplayPrice())){
            productTeaser.setDisplayPrice(fallbackProduct.getDisplayPrice());
        }

    }

    public void injectProductDetailsWithFallback(ProductTeaser teaser, String languageId) {

        var details = languageDAO.getProductDetailsByProductIdAndLanguageId(teaser.getId(), languageId);
        if(details.isEmpty()){
            details = languageDAO.getProductDetailsByProductIdAndLanguageId(teaser.getId(), fallbackLanguage);
        }
        detailHandler.injectTranslatedDetailsIntoProduct(teaser,details);

    }
}
