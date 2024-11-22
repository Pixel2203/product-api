package com.example.firstrestapi.dao;

import com.example.firstrestapi.Database.DBManager;
import com.example.firstrestapi.Database.mysql.models.BaseProductModel;
import com.example.firstrestapi.Database.mysql.models.CategoryModel;
import com.example.firstrestapi.Database.mysql.models.DetailTranslationModel;
import com.example.firstrestapi.Database.mysql.models.ProductTranslationModel;
import com.example.firstrestapi.dto.ProductLanguageTranslation;
import com.example.firstrestapi.dto.ProductDetail;
import com.example.firstrestapi.dto.RegisterProductRequest;
import com.example.firstrestapi.Database.mysql.*;
import com.example.firstrestapi.util.ErrorCode;
import com.example.firstrestapi.util.Utils;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@RequiredArgsConstructor
@Component
public class ProductDAO {


    private final DetailTranslationRepository detailTranslationRepository;
    private final ProductTranslationRepository productTranslationRepository;
    private final BaseProductRepository baseProductRepository;
    private final CategoryRepository categoryRepository;
    private final DBManager dbManager;

    Logger log = LoggerFactory.getLogger(ProductDAO.class);
    public List<BaseProductModel> getProductsByCategory(String categoryNameId){
        String sql = "SELECT products.id, products.imageUrl, products.categoryId, products.price FROM products,categoryids WHERE categoryids.name = '%s' AND categoryids.id = products.categoryId".formatted(categoryNameId);
        return getBaseProducts(sql);
    }
    public List<BaseProductModel> getBaseProductsByIds(List<Integer> productIds){
        String in = Utils.buildIn(productIds);
        String sql = "SELECT products.id, products.imageUrl, products.categoryId, products.price FROM products WHERE id IN %s".formatted(in);
        return getBaseProducts(sql);
    }
    private List<BaseProductModel> getBaseProducts(String query) {
        ResultSet resultSet;
        ArrayList<BaseProductModel> products = new ArrayList<>();
        try{
            Statement statement = dbManager.getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                BaseProductModel product = new BaseProductModel(
                        resultSet.getInt("id"),
                        resultSet.getString("imageUrl"),
                        resultSet.getInt("categoryId"),
                        resultSet.getFloat("price"));
                products.add(product);
            }
            return products;
        }catch (SQLException e){
            log.error("Was not able to resolve BaseProducts with query: {}", query);
        }

        return products;
    }


    public ErrorCode registerProductToDatabase(RegisterProductRequest request){

        List<ProductLanguageTranslation> translations = request.translations();
        if(translations.isEmpty()){
            log.info("Unable to register product because no translations were found");
            return ErrorCode.NO_TRANSLATIONS_PROVIDED;
        }

        var foundCategoryOptional = categoryRepository.findCategoryModelByName(request.categoryIdName());

        Integer categoryId;

        if(foundCategoryOptional.isEmpty()){
            CategoryModel newCategory = new CategoryModel();
            newCategory.setName(request.categoryIdName());

            categoryId = categoryRepository.save(newCategory).getId();
            log.info("A new category='{}' with id='{}' has been created!", request.categoryIdName(),categoryId);
        }else {

            categoryId = foundCategoryOptional.get().getId();
        }



        Integer generatedProductId = insertBaseProduct(request.img(), categoryId, request.price());

        if(Objects.isNull(generatedProductId)){
            log.error("Unable to insert BaseProduct into database");
            return ErrorCode.SQL_ERROR;
        }

        insertProductTranslations(generatedProductId, translations);
        insertDetailTranslations(generatedProductId, translations);
        log.info("Added new product with id='{}'", generatedProductId);
        return ErrorCode.NONE;

    }
    @Nullable
    private Integer insertBaseProduct(String image, int categoryId, float price){
        BaseProductModel productModel = BaseProductModel.builder()
                .categoryId(categoryId)
                .imageUrl(image)
                .price(price)
                .build();
        return  baseProductRepository.save(productModel).getId();
    }
    private void insertProductTranslations(int productId, List<ProductLanguageTranslation> translations){
        for(ProductLanguageTranslation translation : translations){
            ProductTranslationModel translationModel = ProductTranslationModel.builder()
                    .productId(productId)
                    .languageId(translation.languageId())
                    .displayName(translation.displayName())
                    .build();
            productTranslationRepository.save(translationModel);

        }
    }
    private void insertDetailTranslations(int productId, List<ProductLanguageTranslation> translations){
        for(ProductLanguageTranslation translation : translations){
            for(ProductDetail detail : translation.details()){
                DetailTranslationModel detailTranslation = DetailTranslationModel.builder()
                        .productId(productId)
                        .languageId(translation.languageId())
                        .name(detail.displayName())
                        .value(detail.displayValue())
                        .build();
                detailTranslationRepository.save(detailTranslation);
            }
        }
    }





}
