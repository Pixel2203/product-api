package com.example.firstrestapi.DAOs;

import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Records.AddCategoriesRequest;
import com.example.firstrestapi.Records.Category;
import com.example.firstrestapi.Records.LanguageObject;
import com.example.firstrestapi.Records.ProductDetail;
import com.example.firstrestapi.service.ProductService;
import com.mysql.cj.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
    public Optional<List<ProductDTO>> addLanguageProperties(List<ProductDTO> productDTOS, String languageId){
        List<ProductDTO> withProductTranslation = addProductTranslation(productDTOS,languageId,true);
        List<ProductDTO> withProductAndDetailTranslation = addDetailTranslation(withProductTranslation,languageId,true);
        return Optional.of(withProductAndDetailTranslation);
    }





    private List<ProductDTO> addProductTranslation(List<ProductDTO> productDTOS, String languageId, boolean checkData){
        for(ProductDTO product : productDTOS){
            String sql = "SELECT * FROM productTranslation WHERE productId=%s AND languageId='%s'".formatted(product.getId(),languageId);
            try {
                Statement statement = dbManager.getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                if(resultSet.next()){
                    product.setDisplayPrice(resultSet.getString("displayPrice"));
                    product.setDisplayName(resultSet.getString("displayName"));
                }
                if(checkData){
                    checkProductTranslation(product);
                }
            }catch (SQLException e){

            }

        }
        return productDTOS;
    }
    private ProductDTO checkProductTranslation(ProductDTO productDTO){
        // Load fallback
        ProductDTO fallbackProduct = addProductTranslation(List.of(productDTO),fallbackLanguage,false).get(0);
        if(StringUtils.isNullOrEmpty(productDTO.getDisplayName())){
            productDTO.setDisplayName(fallbackProduct.getDisplayName());
        }
        if(StringUtils.isNullOrEmpty(productDTO.getDisplayPrice())){
            productDTO.setDisplayPrice(fallbackProduct.getDisplayPrice());
        }
        return productDTO;
    }
    private List<ProductDTO> addDetailTranslation(List<ProductDTO> productDTOS, String languageId, boolean checkData){
        for(ProductDTO product : productDTOS){
            String sql = "SELECT * FROM detailTranslation WHERE productId=%s AND languageId='%s'".formatted(product.getId(),languageId);
            try{
                Statement statement = dbManager.getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                List<ProductDetail> details = new ArrayList<>();
                while(resultSet.next()){
                    ProductDetail detail = new ProductDetail(resultSet.getString("name"), resultSet.getString("value"));

                    details.add(detail);
                }
                details.sort((detail1, detail2) -> {
                    int length1 = detail1.displayName().length() + detail1.value().length();
                    int length2 = detail2.displayName().length() + detail2.value().length();
                    return Integer.compare(length1, length2);
                });
                product.setDetails(details);
                if(checkData){
                    checkDetailTranslation(product);
                }
            }catch (SQLException ignored){

            }



        }
        return productDTOS;
    }
    private ProductDTO checkDetailTranslation(ProductDTO product){
        ProductDTO productCopy = new ProductDTO(product.getId(),product.getImg(),product.getCategoryId());
        productCopy.setDetails(product.getDetails());
        productCopy.setDisplayPrice(product.getDisplayPrice());
        productCopy.setDisplayName(product.getDisplayName());
        // Loading Fallback
        ProductDTO fallbackProduct = addDetailTranslation(List.of(productCopy),fallbackLanguage,false).get(0);

        if(product.getDetails().size() == 0){
            product.setDetails(fallbackProduct.getDetails());
        }
        return fallbackProduct;
    }


}

