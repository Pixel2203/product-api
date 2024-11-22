package com.example.firstrestapi.service;

import com.example.firstrestapi.Database.mysql.CategoryRepository;
import com.example.firstrestapi.Database.mysql.CategoryTranslationRepository;
import com.example.firstrestapi.Database.mysql.models.CategoryTranslationModel;
import com.example.firstrestapi.EnvConfig;
import com.example.firstrestapi.config.CustomMessageSource;
import com.example.firstrestapi.dto.TranslateCategoryRequest;
import com.example.firstrestapi.request.HttpRequestContext;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.util.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CategoryService extends CService{

    private final CategoryTranslationRepository categoryTranslationRepository;
    private final CategoryRepository categoryRepository;
    private final EnvConfig envConfig;
    public CategoryService(HttpRequestContext requestContext,
                           CustomMessageSource messageSource,
                           CategoryTranslationRepository categoryTranslationRepository,
                           CategoryRepository categoryRepository,
                           EnvConfig envConfig) {
        super(requestContext, messageSource);
        this.categoryTranslationRepository = categoryTranslationRepository;
        this.categoryRepository = categoryRepository;
        this.envConfig = envConfig;
    }


    public EventResponse<?> addCategory(TranslateCategoryRequest request){
        var referencedCategory = categoryRepository.findCategoryModelByName(request.categoryId());
        if(referencedCategory.isEmpty()){
            return EventResponse.failed("Category '%s' doesn't exists".formatted(request.categoryId()), ErrorCode.INVALID_REQUEST);
        }

        int categoryId = referencedCategory.get().getId();
        boolean categoryAlreadyExists = categoryTranslationRepository.existsCategoryTranslationByCategoryIdAndLanguageId(categoryId, request.languageId());

        if(categoryAlreadyExists){
            return EventResponse.failed("Translation for category='%s' in language='%s' already exists!".formatted(categoryId, request.languageId()), ErrorCode.NONE);
        }

        CategoryTranslationModel categoryTranslationModel = new CategoryTranslationModel();
        categoryTranslationModel.setLanguageId(request.languageId());
        categoryTranslationModel.setCategoryId(categoryId);
        categoryTranslationModel.setTranslatedName(request.translation());
        categoryTranslationRepository.save(categoryTranslationModel);
        log.info("Translation for category='%s' added successfully!".formatted(categoryId));
        return EventResponse.withoutResult(true, "Successfully added category translation!", ErrorCode.NONE);
    }


    public EventResponse<?> getAllCategories() {
        String locale = this.requestContext.getLocale();
        var categories = categoryRepository.findAll();
        Map<String, String> categoryMappings = new HashMap<>();
        for(var category : categories){
            var translation = categoryTranslationRepository.findCategoryTranslationModelByCategoryIdAndLanguageId(category.getId(), locale);
            if(translation.isEmpty()){
                translation = categoryTranslationRepository.findCategoryTranslationModelByCategoryIdAndLanguageId(category.getId(), envConfig.getFallbackLanguage());
            }

            if(translation.isEmpty()){
                categoryMappings.put(category.getName(), category.getName());
                continue;
            }
            categoryMappings.put(category.getName(), translation.get().getTranslatedName());
        }

        return new EventResponse<>(true, "Found %s Categories".formatted(categoryMappings.keySet().size()), categoryMappings, ErrorCode.NONE);

    }
}
