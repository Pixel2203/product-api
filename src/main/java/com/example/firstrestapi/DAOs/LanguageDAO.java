package com.example.firstrestapi.DAOs;

import com.example.firstrestapi.DTOs.ProductTeaser;
import com.example.firstrestapi.DTOs.ProductDetail;
import com.example.firstrestapi.Database.mysql.tables.ProductTranslationModel;
import com.example.firstrestapi.Database.mysql.tables.ProductTranslationRepository;
import com.example.firstrestapi.service.ProductService;
import com.example.firstrestapi.util.PriceHelper;
import com.example.firstrestapi.util.Utils;
import com.mysql.cj.util.StringUtils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@RequiredArgsConstructor
public class LanguageDAO {

    private static final Logger log = LoggerFactory.getLogger(LanguageDAO.class);
    private final ProductTranslationRepository productTranslationRepository;

    /**
     * Injects displayPrice and displayName into the given products
     * @param productTeasers ProductDTOs which will be modified
     * @param languageId the language in which it will be translated
     */
    public void injectPriceAndName(List<ProductTeaser> productTeasers, String languageId) {
        if(productTeasers.isEmpty() || StringUtils.isNullOrEmpty(languageId)){
            return;
        }
        injectDisplayPrice(productTeasers, languageId);
        injectDisplayName(productTeasers, languageId);

    }

    private void injectDisplayName(List<ProductTeaser> productTeasers, String languageId){
        for(ProductTeaser productTeaser : productTeasers) {
            productTranslationRepository.findProductTranslationModelByProductIdAndLanguageId(productTeaser.getId(), languageId).ifPresent(productTranslationModel -> {
                productTeaser.setDisplayName(productTranslationModel.getDisplayName());
                productTeaser.setLanguageModel(languageId);
            });
        }
    }

    private void injectDisplayPrice(List<ProductTeaser> productTeasers, String languageId){
        PriceHelper priceHelper = getPriceInformationForLanguage(languageId);
        if(Objects.isNull(priceHelper)){
            priceHelper = new PriceHelper("", "€", false);
        }

        for(ProductTeaser productTeaser : productTeasers){
            productTeaser.setDisplayPrice(priceHelper.buildPrice(productTeaser.getPrice()));
        }
    }

    @Nullable
    public PriceHelper getPriceInformationForLanguage(String language) {
        Map<String, String> priceInformation = new HashMap<>();
        String sql = "SELECT * FROM price_mapping WHERE country='%s'".formatted(language);
        try {
            Statement statement = ProductService.dbManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if(!resultSet.next()){
                return null;
            }
            return new PriceHelper(
                    resultSet.getString("prefix"),
                    resultSet.getString("suffix"),
                    Objects.equals(resultSet.getString("use_dot"), "1")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Nonnull
    public List<ProductDetail> getProductDetailsByProductIdAndLanguageId(int productId, String languageId){
        String sql = "SELECT * FROM detailTranslation WHERE productId=%s AND languageId='%s'".formatted(productId, languageId);
        try {
            ResultSet resultSet;
            Statement statement = ProductService.dbManager.getConnection().createStatement();
            resultSet = statement.executeQuery(sql);
            List<ProductDetail> details = new ArrayList<>();
            while (resultSet.next()) {
                ProductDetail detail = new ProductDetail(resultSet.getString("name"), resultSet.getString("value"));
                details.add(detail);
            }
            return details;
        } catch (SQLException ignored) {
            log.error("Unable to load product details for product {}" , productId);
        }
        return List.of();
    }






}

