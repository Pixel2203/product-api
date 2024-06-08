package com.example.firstrestapi.DAOs;

import com.example.firstrestapi.DTOs.CartProductDTO;
import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Records.ProductLanguageTranslation;
import com.example.firstrestapi.Records.ProductDetail;
import com.example.firstrestapi.Records.RegisterProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.ssl.PemSslBundleProperties;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static com.example.firstrestapi.FirstRestApiApplication.dbManager;

public class ProductDAO {
    Logger logger = LoggerFactory.getLogger(ProductDAO.class);
    public Optional<List<ProductDTO>> getProductsByCategory(String categoryNameId){
        try {
            String sql = "SELECT products.id, products.imageUrl, products.categoryId FROM products,categoryids WHERE categoryids.category_name = '%s' AND categoryids.id = products.categoryId".formatted(categoryNameId);
            Statement statement = dbManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<ProductDTO> productDTOS = new ArrayList<>();
            while(resultSet.next()){
                ProductDTO productDTO = new ProductDTO(
                        resultSet.getInt("id"),
                        resultSet.getString("imageUrl"),
                        resultSet.getInt("categoryId")
                );
                productDTOS.add(productDTO);
            }
            return Optional.of(productDTOS);
        }catch (Exception e){
            logger.error("Was not able to resolve products by category: " + categoryNameId);
        }
        return Optional.empty();
    }
    public Optional<Map<Integer, CartProductDTO>> getProductsByIds(Map<Integer,Integer> idAmountMap){
        try {
            String in = buildIn(idAmountMap.keySet());
            String sql = "SELECT products.id, products.imageUrl, products.categoryId FROM products WHERE id IN %s".formatted(in);
            Statement statement = dbManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            Map<Integer,CartProductDTO> products = new HashMap<>();
            while(resultSet.next()){
                int pId = resultSet.getInt("id");
                ProductDTO product = new ProductDTO(
                                        pId,
                                        resultSet.getString("imageUrl"),
                                        resultSet.getInt("categoryId"));
                products.put(pId, new CartProductDTO(product, idAmountMap.get(pId))) ;
            }
            return Optional.of(products);
        }catch (Exception e){
            logger.error("Was not able to resolve products by ids: " + idAmountMap.keySet());
        }
        return Optional.empty();
    }
    private String buildIn(Set<?> info){
        String sql = "(";
        for (Object o : info) {
            sql += o.toString() + ",";
        }
        return sql.substring(0, sql.length() - 1) + ")";
    }




    public Optional<String> registerProductToDatabase(RegisterProductRequest request){

        List<ProductLanguageTranslation> translations = request.translations();
        if(translations.size() == 0){
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
        try(PreparedStatement statement = dbManager.getConnection().prepareStatement(insertProduct,Statement.RETURN_GENERATED_KEYS)){
            statement.executeUpdate();
            try(ResultSet keys = statement.getGeneratedKeys()){
                if(keys.next()){
                    int generatedProductId = (int) keys.getLong(1);
                    return generatedProductId;
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
                Statement statement = dbManager.getConnection().createStatement();
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
                stringBuilder.append(productId + "','");
                stringBuilder.append(translation.languageId() + "','");
                stringBuilder.append(detail.displayName() + "','");
                stringBuilder.append(detail.value() + "')");
                try{
                    Statement statement = dbManager.getConnection().createStatement();
                    statement.execute(stringBuilder.toString());
                }catch (SQLException e){
                    return false;
                }
            }
        }
        return true;

    }
    private List<ProductDetail> getDetailsForProductsByIds(List<Integer> productIds){
        //String sql = "SELECT".formatted()
        return List.of();
    }
}
