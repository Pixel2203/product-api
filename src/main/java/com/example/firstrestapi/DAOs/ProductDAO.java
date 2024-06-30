package com.example.firstrestapi.DAOs;

import com.example.firstrestapi.DTOs.BaseProduct;
import com.example.firstrestapi.DTOs.ProductLanguageTranslation;
import com.example.firstrestapi.DTOs.ProductDetail;
import com.example.firstrestapi.DTOs.RegisterProductRequest;
import com.example.firstrestapi.service.ProductService;
import com.example.firstrestapi.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ProductDAO {

    Logger logger = LoggerFactory.getLogger(ProductDAO.class);
    public Optional<List<BaseProduct>> getProductsByCategory(String categoryNameId){
        try {
            String sql = "SELECT products.id, products.imageUrl, products.categoryId FROM products,categoryids WHERE categoryids.category_name = '%s' AND categoryids.id = products.categoryId".formatted(categoryNameId);
            Statement statement = ProductService.dbManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<BaseProduct> baseProducts = new ArrayList<>();
            while(resultSet.next()){
                BaseProduct baseProduct = new BaseProduct(
                        resultSet.getInt("id"),
                        resultSet.getString("imageUrl"),
                        resultSet.getInt("categoryId")
                );
                baseProducts.add(baseProduct);
            }
            return Optional.of(baseProducts);
        }catch (Exception e){
            logger.error("Was not able to resolve products by category: {}", categoryNameId);
        }
        return Optional.empty();
    }
    /*
    public Optional<Map<Integer, CartProductDTO>> getProductsByIds(Map<Integer,Integer> idAmountMap){
        try {
            String in = buildIn(idAmountMap.keySet());
            String sql = "SELECT products.id, products.imageUrl, products.categoryId FROM products WHERE id IN %s".formatted(in);
            ResultSet resultSet;
            try (Statement statement = dbManager.getConnection().createStatement()) {
                resultSet = statement.executeQuery(sql);
            }
            Map<Integer,CartProductDTO> products = new HashMap<>();
            while(resultSet.next()){
                int pId = resultSet.getInt("id");
                ProductDTO product = new ProductDTO(
                                        pId,
                                        resultSet.getString("imageUrl"),
                                        resultSet.getInt("categoryId"));
                products.put(pId, new CartProductDTO(product, idAmountMap.get(pId), 0f)) ;
            }
            return Optional.of(products);
        }catch (Exception e){
            logger.error("Was not able to resolve products by ids: {}", idAmountMap.keySet());
        }
        return Optional.empty();
    }

     */
    public Optional<List<BaseProduct>> getBaseProducts(List<Integer> productIds){
        try {
            String in = Utils.buildIn(productIds);
            String sql = "SELECT products.id, products.imageUrl, products.categoryId FROM products WHERE id IN %s".formatted(in);
            ResultSet resultSet;
            Statement statement = ProductService.dbManager.getConnection().createStatement();
            resultSet = statement.executeQuery(sql);
            List<BaseProduct> products = new ArrayList<>();
            while(resultSet.next()){
                int pId = resultSet.getInt("id");
                BaseProduct product = new BaseProduct(
                        resultSet.getInt("id"),
                        resultSet.getString("imageUrl"),
                        resultSet.getInt("categoryId"));
                products.add(product);
            }
            return Optional.of(products);
        }catch (Exception e){
            logger.error("Was not able to resolve products by ids: {}", productIds.toArray());
        }
        return Optional.empty();
    }







    public Optional<String> registerProductToDatabase(RegisterProductRequest request){

        List<ProductLanguageTranslation> translations = request.translations();
        if(translations.isEmpty()){
            return Optional.of("Must Provide at least one translation!");
        }


        int generatedProductId = addToProductDatabase(request.img(), request.categoryId());
        if(addToProductTranslationDatabase(generatedProductId, translations) &&
                addToProductDetailTranslationDatabase(generatedProductId, translations)){
            return Optional.of(request.toString());
        }

        return Optional.empty();
    }
    private int addToProductDatabase(String image, int categoryId){
        String insertProduct = String.format("INSERT INTO products (imageUrl , categoryId) VALUES ('%s', '%s')", image, categoryId);
        try(PreparedStatement statement = ProductService.dbManager.getConnection().prepareStatement(insertProduct,Statement.RETURN_GENERATED_KEYS)){
            statement.executeUpdate();
            try(ResultSet keys = statement.getGeneratedKeys()){
                if(keys.next()){
                    return (int) keys.getLong(1);
                }
            }
        }catch (SQLException e){
            logger.error("Failed adding product to database");
        }
        return 0;
    }
    private boolean addToProductTranslationDatabase(int productId, List<ProductLanguageTranslation> translations){
        for(ProductLanguageTranslation translation : translations){
/*
            String insertProductTranslation =
                    "INSERT INTO productTranslation (productId, languageId, displayName, displayPrice) VALUES ("+
                            "'" + productId + "', " +
                            "'" + translation.languageId() + "', " +
                            "'" + translation.displayName() + "', " +
                            "'" + translation.displayPrice() + "')";

 */
            String insertProductTranslation = String.format("INSERT INTO productTranslation (productId, languageId, displayName, displayPrice) VALUES ('%s','%s','%s','%s')", productId, translation.languageId(), translation.displayName(), translation.displayPrice());
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
