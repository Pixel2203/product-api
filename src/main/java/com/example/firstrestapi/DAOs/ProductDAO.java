package com.example.firstrestapi.DAOs;

import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Records.ProductLanguageTranslation;
import com.example.firstrestapi.Records.ProductDetail;
import com.example.firstrestapi.Records.RegisterProductRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static com.example.firstrestapi.FirstRestApiApplication.dbManager;

public class ProductDAO {

    public Optional<List<ProductDTO>> getProductsByCategory(int categoryId){
        Optional optional = Optional.empty();
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT * FROM products WHERE categoryId=" + categoryId);
            Statement statement = dbManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
            List<ProductDTO> productDTOS = new ArrayList<>();
            while(resultSet.next()){
                ProductDTO productDTO = new ProductDTO(
                        resultSet.getInt("id"),
                        resultSet.getString("imageUrl"),
                        resultSet.getInt("categoryId")
                );
                productDTOS.add(productDTO);
            }
            optional = Optional.of(productDTOS);
        }catch (Exception e){
            System.out.println(e);
        }
        return  optional;
    }
    public Optional<String> registerProductToDatabase(RegisterProductRequest request){
        Optional optional = Optional.empty();
        ProductDTO newProduct = new ProductDTO(request.id(), request.img(),request.categoryId());
        List<ProductLanguageTranslation> translations = request.translations();
        if(translations.size() == 0){
            optional = Optional.of("Must Provide at least one translation!");
            return  optional;
        }

        if(addToProductDatabase(newProduct) &&
            addToProductTranslationDatabase(newProduct.getId(), translations) &&
                addToProductDetailTranslationDatabase(newProduct.getId(), translations)){
            optional = Optional.of(request.toString());
        }

        return  optional;
    }
    private boolean addToProductDatabase(ProductDTO newProduct){
        String insertProduct =
                "INSERT INTO products (id, imageUrl , categoryId) VALUES (" +
                        "'" + newProduct.getId() + "'," +
                        "'" + newProduct.getImg() + "'," +
                        "'" + newProduct.getCategoryId() + "')";
        try{
            Statement statement = dbManager.getConnection().createStatement();
            statement.execute(insertProduct);
        }catch (SQLException e){
            return false;
        }

        return true;
    }
    private boolean addToProductTranslationDatabase(int productId, List<ProductLanguageTranslation> translations){
        for(ProductLanguageTranslation translation : translations){

            String insertProductTranslation =
                    "INSERT INTO productTranslation (productId, languageId, displayName, displayPrice) VALUES ("+
                            "'" + productId + "', " +
                            "'" + translation.languageId() + "', " +
                            "'" + translation.displayName() + "', " +
                            "'" + translation.displayPrice() + "')";
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
                stringBuilder.append(detail.detailName() + "','");
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
}
