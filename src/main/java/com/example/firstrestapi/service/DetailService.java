package com.example.firstrestapi.service;

import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Records.ProductDetail;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.firstrestapi.FirstRestApiApplication.dbManager;
import static com.example.firstrestapi.service.LanguageService.fallbackLanguage;
@Service
public class DetailService {
    public void addTranslatedDetailsWithFallback(List<ProductDTO> productDTOS, String languageId, boolean checkData) {
        for (ProductDTO product : productDTOS) {
            String sql = "SELECT * FROM detailTranslation WHERE productId=%s AND languageId='%s'".formatted(product.getId(), languageId);
            try {
                Statement statement = dbManager.getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                List<ProductDetail> details = new ArrayList<>();
                while (resultSet.next()) {
                    ProductDetail detail = new ProductDetail(resultSet.getString("name"), resultSet.getString("value"));
                    details.add(detail);
                }
                details.sort((detail1, detail2) -> {
                    int length1 = detail1.displayName().length() + detail1.value().length();
                    int length2 = detail2.displayName().length() + detail2.value().length();
                    return Integer.compare(length1, length2);
                });
                product.setDetails(details);
                if (checkData) {
                    checkDetailTranslation(product);
                }
            } catch (SQLException ignored) {

            }
        }
    }

    public ProductDTO checkDetailTranslation(ProductDTO product){
        ProductDTO productCopy = ProductDTO.copyFrom(product);
        // Loading Fallback
        ProductDTO fallbackProduct = addTranslatedDetailsWithFallback(List.of(productCopy),fallbackLanguage,false).get(0);

        if(product.getDetails().isEmpty()){
            product.setDetails(fallbackProduct.getDetails());
        }
        return fallbackProduct;
    }



}
