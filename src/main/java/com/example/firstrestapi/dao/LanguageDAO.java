package com.example.firstrestapi.dao;

import com.example.firstrestapi.Database.DBManager;
import com.example.firstrestapi.Database.mysql.ProductTranslationRepository;
import com.example.firstrestapi.Database.mysql.models.ProductTranslationModel;
import com.example.firstrestapi.dto.ProductDetail;
import com.example.firstrestapi.util.PriceHelper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class LanguageDAO {

    private static final Logger log = LoggerFactory.getLogger(LanguageDAO.class);
    private final ProductTranslationRepository productTranslationRepository;
    private final DBManager dbManager;

    public Optional<ProductTranslationModel> getProductTranslationModel(int teaserId, String languageId){
        return productTranslationRepository.findProductTranslationModelByProductIdAndLanguageId(teaserId, languageId);
    }



    @Nullable
    public PriceHelper getPriceInformationForLanguage(String language) {
        String sql = "SELECT * FROM price_mapping WHERE country='%s'".formatted(language);
        try {
            Statement statement = dbManager.getConnection().createStatement();
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
            Statement statement = dbManager.getConnection().createStatement();
            resultSet = statement.executeQuery(sql);
            List<ProductDetail> details = new ArrayList<>();
            while (resultSet.next()) {
                ProductDetail detail = new ProductDetail(resultSet.getString("name"), resultSet.getString("translated_value"));
                details.add(detail);
            }
            return details;
        } catch (SQLException ignored) {
            log.error("Unable to load product details for product {}" , productId);
        }
        return List.of();
    }






}

