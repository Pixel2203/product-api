package com.example.firstrestapi.dao;

import com.example.firstrestapi.Database.DBManager;
import com.example.firstrestapi.Database.mysql.*;
import com.example.firstrestapi.Database.mysql.models.*;
import com.example.firstrestapi.dto.ProductDetail;
import com.example.firstrestapi.dto.ProductLanguageTranslation;
import com.example.firstrestapi.dto.RegisterProductRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CategoryDAO {
    private final CategoryRepository categoryRepository;
    private final CategoryTranslationRepository categoryTranslationRepository;
    public String translateCategoryIdName(String categoryIdName, String locale){
        var category = categoryRepository.findCategoryModelByName(categoryIdName);
        if(category.isEmpty()) return categoryIdName;

        CategoryModel categoryModel = category.get();

        Optional<CategoryTranslationModel> model = categoryTranslationRepository.findCategoryTranslationModelByCategoryIdAndLanguageId(categoryModel.getId(), locale);

        return model.map(CategoryTranslationModel::getTranslatedName)
                .orElse(categoryIdName);

    }
}
