package com.example.firstrestapi.service;
import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Records.*;
import com.example.firstrestapi.api.controller.LanguageController;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {
    public static final String fallbackLanguage = "EN";
    public Optional<List<Category>> getCategoriesByLanguageId(String languageId){
        return new LanguageDAO().getCategoryTranslationByLanguageId(languageId);
    }
    public Optional<List<LanguageObject>> getLanguagesByLanguageId(String languageId){
        Optional optional = new LanguageDAO().getLanguagesById(languageId);
        return optional;
    }
    public Optional<List<ProductDTO>> addLanguageDetailsToProduct(List<ProductDTO> productDTOS, String languageId){
        LanguageDAO languageDAO = new LanguageDAO();
        List<ProductDTO> productDTOList = languageDAO.addLanguageProperties(productDTOS,languageId).get()
                .stream()
                .filter(product -> product != null)
                .toList();
        return Optional.of(productDTOList);
    }

    public String addCategoriesByLanguageId(AddCategoriesRequest request) {
        if(new LanguageDAO().addCategoriesByLanguageId(request)){
            return "Categories successfully added!";
        }
        return "Categories could not have been added!";
    }
    public LanguageData getFullLanguagePack(String languageId){
        final String fallbackLanguage = "EN";
        Optional<List<Category>> categories = getCategoriesByLanguageId(languageId);
        Optional<List<LanguageObject>> languages = getLanguagesByLanguageId(languageId);
        List<Category> categoryList;
        List<LanguageObject> languageList;
        if(categories.isPresent() && categories.get().size() > 0){
            categoryList = categories.get();
        }else{
            // Fallback
            categoryList = getCategoriesByLanguageId(fallbackLanguage).get();
        }
        if(languages.isPresent() &&  languages.get().size() > 0){
           languageList = languages.get();
        }else{
            languageList = getLanguagesByLanguageId(fallbackLanguage).get();
        }
        HeaderLangData headerLangData = new HeaderLangData(categoryList,languageList);
        return new LanguageData(languageId,headerLangData);
    }
}
