package com.example.firstrestapi.DAOs;

import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Database.DBManager;
import com.example.firstrestapi.Records.ProductDetail;
import com.example.firstrestapi.service.ProductService;
import com.example.firstrestapi.util.PriceHelper;
import com.example.firstrestapi.util.Utils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class LanguageDAO {

    private static final Logger log = LoggerFactory.getLogger(LanguageDAO.class);

    /**
     * Injects displayPrice and displayName into the given products
     * @param productDTOS ProductDTOs which will be modified
     * @param languageId the language in which it will be translated
     */
    public void injectDisplayNameAndPrice(List<ProductDTO> productDTOS, String languageId) throws Exception{
        PriceHelper priceHelper = getPriceInformationForLanguage(languageId);
        if(Objects.isNull(priceHelper)){
            priceHelper = new PriceHelper("", "€", false);
        }
        List<Integer> productIds = productDTOS.stream().map(ProductDTO::getId).toList();

        String sql = "SELECT * FROM productTranslation WHERE productTranslation.productId IN %s AND productTranslation.languageId='%s'".formatted(Utils.buildIn(productIds),languageId);


        Statement statement = ProductService.dbManager.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while(resultSet.next()){
            int productId = resultSet.getInt("productId");
            String displayName = resultSet.getString("displayName");
            float price = resultSet.getFloat("displayPrice");
            String displayPrice = priceHelper.buildPrice(price);

            productDTOS.stream().filter(productDTO -> productDTO.getId() == productId).forEach(productDTO -> {
                productDTO.setDisplayName(displayName);
                productDTO.setDisplayPrice(displayPrice);
            });
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

