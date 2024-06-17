package com.example.firstrestapi.DAOs;

import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Records.AddCategoriesRequest;
import com.example.firstrestapi.Records.Category;
import com.example.firstrestapi.Records.LanguageObject;
import com.example.firstrestapi.Records.ProductDetail;
import com.example.firstrestapi.service.ProductService;
import com.example.firstrestapi.util.PriceHelper;
import com.example.firstrestapi.util.Utils;
import com.mysql.cj.util.StringUtils;
import jakarta.annotation.Nullable;

import javax.swing.plaf.nimbus.State;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static com.example.firstrestapi.FirstRestApiApplication.dbManager;
import static com.example.firstrestapi.service.LanguageService.fallbackLanguage;

public class LanguageDAO {

    public Optional<HashMap<String,LanguageObject>> getLanguagesById(String languageId) {
        String sql = "SELECT * FROM languageTranslation WHERE id = '" + languageId + "'";
        String getFlagsUrlSql = "SELECT * FROM flags";
        HashMap<String, String> foundFlags = new HashMap<>();
        HashMap<String, LanguageObject> foundLanguages = new HashMap<>();
        try (Statement statement = dbManager.getConnection().createStatement();
             ResultSet allFlags = statement.executeQuery(getFlagsUrlSql)) {

            while (allFlags.next()) {
                foundFlags.put(allFlags.getString("languageId"), allFlags.getString("flagUrl"));
            }

            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    String language = resultSet.getString("language");
                    String flagUrl = foundFlags.getOrDefault(language, null);
                    foundLanguages.put(language ,new LanguageObject(language, resultSet.getString("name"), flagUrl));

                }
            }

            return Optional.of(foundLanguages);

        } catch (SQLException e) {
            return Optional.empty();
        }
    }


    public Optional<HashMap<Integer,Category>> getCategoryTranslationByLanguageId(String languageId){

        String sql = "SELECT * FROM categories,categoryids WHERE languageId = '%s' AND categories.categoryId = categoryids.id".formatted(languageId);
        try{
            Statement statement = dbManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            HashMap<Integer,Category> foundCategories = new HashMap<>();
            while(resultSet.next()){
                Category category = new Category(
                        resultSet.getInt("categoryId"),
                        resultSet.getString("name"),
                        resultSet.getString("category_name")
                );
                foundCategories.put(resultSet.getInt("categoryId"),category);
            }
            return Optional.of(foundCategories);
        }catch (SQLException e){}

        return Optional.empty();
    }
    public boolean addCategoriesByLanguageId(AddCategoriesRequest request) {
        String languageId = request.languageId();
        for(Category category : request.categories()){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("INSERT INTO categories (languageId,categoryId,name) VALUES ('");
            stringBuilder.append(languageId + "', '" + category.id() + "' , '");
            stringBuilder.append(category.displayName() + "')");
            try{
                Statement statement = dbManager.getConnection().createStatement();
                statement.execute(stringBuilder.toString());
            }catch (SQLException e){
                return false;
            }
        }
        return true;

    }

    public List<ProductDTO> addProductTranslation(List<ProductDTO> productDTOS, String languageId, boolean checkData){
        PriceHelper priceHelper = getPriceInformationForLanguage(languageId);
        if(Objects.isNull(priceHelper)){
            priceHelper = new PriceHelper("", "€", false);
        }
        for(ProductDTO product : productDTOS){
            String sql = "SELECT * FROM productTranslation,price_mapping WHERE productTranslation.productId=%s AND productTranslation.languageId='%s' AND productTranslation.languageId=price_mapping.country".formatted(product.getId(),languageId);
            try {
                Statement statement = dbManager.getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                if(resultSet.next()){
                    float price = resultSet.getFloat("displayPrice");
                    product.setDisplayPrice(priceHelper.buildPrice(price));
                    product.setDisplayName(resultSet.getString("displayName"));
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

        }
        return productDTOS;
    }
    @Nullable
    private PriceHelper getPriceInformationForLanguage(String language) {
        Map<String, String> priceInformation = new HashMap<>();
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






}

