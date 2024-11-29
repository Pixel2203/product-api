package com.example.firstrestapi;

import com.example.firstrestapi.Database.mysql.models.ProductTranslationModel;
import com.example.firstrestapi.dao.LanguageDAO;
import com.example.firstrestapi.dto.ProductTeaser;
import com.example.firstrestapi.handler.FallbackHandler;
import com.example.firstrestapi.handler.LanguageHandler;
import com.example.firstrestapi.util.PriceHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class FallbackHandlerTests {

    @Test
    public void testInjectDisplayPrice() {
        ProductTeaser teaser = new ProductTeaser(1,"",23,399);
        PriceHelper defaultPriceHelper = PriceHelper.Default();
        // Mocking
        LanguageDAO languageDAO = Mockito.mock(LanguageDAO.class);
        Mockito.doReturn(PriceHelper.Default())
                .when(languageDAO)
                .getPriceInformationForLanguage("de");

        FallbackHandler fallbackHandler = new FallbackHandler(languageDAO);

        fallbackHandler.injectDisplayPriceWithFallback(teaser, "de");
        assert teaser.getDisplayPrice().equals(defaultPriceHelper.buildPrice(teaser.getPrice()));
    }

    @Test
    public void testInjectDisplayPriceWithFallbackToDefault() {
        LanguageDAO languageDAO = Mockito.mock(LanguageDAO.class);
        Mockito.doReturn(null)
                .when(languageDAO)
                .getPriceInformationForLanguage("en");

        FallbackHandler fallbackHandler = new FallbackHandler(languageDAO);

        ProductTeaser teaser = new ProductTeaser(1,"",23,399);

        fallbackHandler.injectDisplayPriceWithFallback(teaser, "en");

        assert teaser.getDisplayPrice().equals(PriceHelper.Default().buildPrice(teaser.getPrice()));
    }

    @Test
    public void testInjectDisplayNameAndModel(){
        ProductTeaser teaser = new ProductTeaser(12,"",23,399);
        ProductTranslationModel translationModel = ProductTranslationModel.builder()
                .displayName("MyTest")
                .languageId("es")
                .build();

        LanguageDAO languageDAO = Mockito.mock(LanguageDAO.class);
        Mockito.doReturn(Optional.of(translationModel)).when(languageDAO).getProductTranslationModel(teaser.getId(),"es");
        FallbackHandler fallbackHandler = new FallbackHandler(languageDAO);

        fallbackHandler.injectDisplayNameAndModel(teaser,"es");

        assert teaser.getDisplayName().equals("MyTest");
        assert teaser.getLanguageModel().equals("es");
    }

    @Test
    public void testInjectDisplayPriceAndDisplayNameWithFallback(){
        ProductTeaser teaser = new ProductTeaser(12,"",23,399);
        ProductTranslationModel translationModel = ProductTranslationModel.builder()
                .displayName("fallback")
                .languageId(LanguageHandler.fallbackLanguage)
                .build();

        LanguageDAO languageDAO = Mockito.mock(LanguageDAO.class);
        Mockito.doReturn(Optional.empty())
                .when(languageDAO)
                .getProductTranslationModel(teaser.getId(),"de");

        Mockito.doReturn(Optional.of(translationModel))
                .when(languageDAO)
                .getProductTranslationModel(teaser.getId(),LanguageHandler.fallbackLanguage);

        FallbackHandler fallbackHandler = new FallbackHandler(languageDAO);
        fallbackHandler.injectDisplayPriceAndDisplayNameWithFallback(teaser,"de");

        assert teaser.getDisplayName().equals("fallback");
        assert teaser.getLanguageModel().equals(LanguageHandler.fallbackLanguage);
        assert teaser.getDisplayPrice().equals(PriceHelper.Default().buildPrice(teaser.getPrice()));
    }
}
