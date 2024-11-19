package com.example.firstrestapi.DAOs;

import com.example.firstrestapi.DTOs.BaseProduct;
import com.example.firstrestapi.DTOs.ProductLanguageTranslation;
import com.example.firstrestapi.DTOs.ProductDetail;
import com.example.firstrestapi.DTOs.RegisterProductRequest;
import com.example.firstrestapi.service.ProductService;
import com.example.firstrestapi.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ProductDAO {

    Logger log = LoggerFactory.getLogger(ProductDAO.class);
    public Optional<List<BaseProduct>> getProductsByCategory(String categoryNameId){
        try {
            String sql = "SELECT products.id, products.imageUrl, products.categoryId, products.price FROM products,categoryids WHERE categoryids.category_name = '%s' AND categoryids.id = products.categoryId".formatted(categoryNameId);
            Statement statement = ProductService.dbManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return getBaseProducts(resultSet);
        }catch (Exception e){
            log.error("Was not able to resolve products by category: {}", categoryNameId);
        }
        return Optional.empty();
    }

    public Optional<List<BaseProduct>> getBaseProducts(List<Integer> productIds){
        try {
            String in = Utils.buildIn(productIds);
            String sql = "SELECT products.id, products.imageUrl, products.categoryId, products.price FROM products WHERE id IN %s".formatted(in);
            ResultSet resultSet;
            Statement statement = ProductService.dbManager.getConnection().createStatement();
            resultSet = statement.executeQuery(sql);
            return getBaseProducts(resultSet);
        }catch (Exception e){
            log.error("Was not able to resolve products by ids: {}", productIds.toArray());
        }
        return Optional.empty();
    }

    @NotNull
    private Optional<List<BaseProduct>> getBaseProducts(ResultSet resultSet) throws SQLException {
        List<BaseProduct> products = new ArrayList<>();
        while(resultSet.next()){
            BaseProduct product = new BaseProduct(
                    resultSet.getInt("id"),
                    resultSet.getString("imageUrl"),
                    resultSet.getInt("categoryId"),
                    resultSet.getFloat("price"));
            products.add(product);
        }
        return Optional.of(products);
    }


    public Optional<String> registerProductToDatabase(RegisterProductRequest request){

        List<ProductLanguageTranslation> translations = request.translations();
        if(translations.isEmpty()){

            return Optional.of("Must Provide at least one translation!");
        }


        int generatedProductId = addToProductDatabase(request.img(), request.categoryId(), request.price());
        if(addToProductTranslationDatabase(generatedProductId, translations) &&
                addToProductDetailTranslationDatabase(generatedProductId, translations)){
            return Optional.of(request.toString());
        }

        return Optional.empty();
    }
    private int addToProductDatabase(String image, int categoryId, float price){
        String insertProduct = String.format("INSERT INTO products (imageUrl , categoryId, price) VALUES ('%s', '%s', '%s')", image, categoryId, price);
        try(PreparedStatement statement = ProductService.dbManager.getConnection().prepareStatement(insertProduct,Statement.RETURN_GENERATED_KEYS)){
            statement.executeUpdate();
            try(ResultSet keys = statement.getGeneratedKeys()){
                if(keys.next()){
                    return (int) keys.getLong(1);
                }
            }
        }catch (SQLException e){
            log.error("Failed adding product to database");
        }
        return 0;
    }
    private boolean addToProductTranslationDatabase(int productId, List<ProductLanguageTranslation> translations){
        for(ProductLanguageTranslation translation : translations){

            String insertProductTranslation = String.format("INSERT INTO productTranslation (productId, languageId, displayName) VALUES ('%s','%s','%s')", productId, translation.languageId(), translation.displayName());
            try{
                Statement statement = ProductService.dbManager.getConnection().createStatement();
                statement.execute(insertProductTranslation);
            }catch (SQLException e){
                return false;
            }

        }
        return true;
    }
    private boolean addToProductDetailTranslationDatabase(int productId, List<ProductLanguageTranslation> translations){

        for(ProductLanguageTranslation translation : translations){
            for(ProductDetail detail : translation.details()){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("INSERT INTO detailTranslation (productId, languageId, name, value) VALUES ('");
                stringBuilder.append(productId).append("','");
                stringBuilder.append(translation.languageId()).append("','");
                stringBuilder.append(detail.displayName()).append("','");
                stringBuilder.append(detail.value()).append("')");
                try{
                    Statement statement = ProductService.dbManager.getConnection().createStatement();
                    statement.execute(stringBuilder.toString());
                }catch (SQLException e){
                    return false;
                }
            }
        }
        return true;

    }





}
