package com.example.firstrestapi.handler;

import com.example.firstrestapi.Database.mysql.models.ProductTranslationModel;
import com.example.firstrestapi.dao.LanguageDAO;
import com.example.firstrestapi.dto.ProductTeaser;
import com.example.firstrestapi.util.PriceHelper;
import com.mysql.cj.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

import static com.example.firstrestapi.handler.LanguageHandler.fallbackLanguage;

public class FallbackHandler {
    private final LanguageDAO languageDAO;
    private final DetailHandler detailHandler;

    public FallbackHandler(LanguageDAO languageDAO) {
        this.languageDAO = languageDAO;
        this.detailHandler = new DetailHandler();
    }

    public void injectDisplayPriceAndDisplayNameWithFallback(@NotNull ProductTeaser productTeaser, String languageId) {

        this.injectDisplayPriceWithFallback(productTeaser, languageId);
        this.injectDisplayNameAndModel(productTeaser, languageId);

        if(StringUtils.isNullOrEmpty(productTeaser.getDisplayName())){
            this.injectDisplayNameAndModel(productTeaser, fallbackLanguage);
        }
    }

    public void injectDisplayNameAndModel(ProductTeaser productTeaser, String languageId) {
        Optional<ProductTranslationModel> translationModel = languageDAO.getProductTranslationModel(productTeaser.getId(),languageId);
        translationModel.ifPresent(productTranslationModel -> {
            productTeaser.setDisplayName(productTranslationModel.getDisplayName());
            productTeaser.setLanguageModel(languageId);
        });
    }

    public void injectDisplayPriceWithFallback(ProductTeaser productTeaser, String languageId) {
        PriceHelper priceHelper = languageDAO.getPriceInformationForLanguage(languageId);
        if(Objects.isNull(priceHelper)){
            priceHelper = languageDAO.getPriceInformationForLanguage(fallbackLanguage);

            if(Objects.isNull(priceHelper)){
                priceHelper = PriceHelper.Default();
            }
        }
        productTeaser.setDisplayPrice(priceHelper.buildPrice(productTeaser.getPrice()));
    }

    public void injectProductDetailsWithFallback(ProductTeaser teaser, String languageId) {

        var details = languageDAO.getProductDetailsByProductIdAndLanguageId(teaser.getId(), languageId);
        if(details.isEmpty()){
            details = languageDAO.getProductDetailsByProductIdAndLanguageId(teaser.getId(), fallbackLanguage);
        }
        detailHandler.injectTranslatedDetailsIntoProduct(teaser,details);

    }
}
