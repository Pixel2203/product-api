package com.example.firstrestapi.service;
import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Records.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LanguageService {
    public static final String fallbackLanguage = "EN";
    public Optional<List<Category>> getCategoriesByLanguageId(String languageId){
        return new LanguageDAO().getCategoryTranslationByLanguageId(languageId);
    }
    public Optional<List<LanguageObject>> getLanguagesByLanguageId(String languageId){
        Optional<HashMap<String,LanguageObject>> languageTranslations = new LanguageDAO().getLanguagesById(languageId);
        if(languageTranslations.isEmpty()){
            return Optional.empty();
        }
        Optional<HashMap<String,LanguageObject>> fallbackTranslations = new LanguageDAO().getLanguagesById(fallbackLanguage);
        if(fallbackTranslations.isEmpty()){
            return Optional.empty();
        }
        HashMap<String,LanguageObject> fallback = fallbackTranslations.get();
        HashMap<String,LanguageObject> foundTranslations = languageTranslations.get();
        List<LanguageObject> missingLanguages = fallback.values().stream()
                .map(LanguageObject::langId).map(foundTranslations::get).filter(Objects::isNull).toList();

        List<LanguageObject> translations = new ArrayList<>(foundTranslations.values().stream().toList());
        translations.addAll(missingLanguages);
        return Optional.of(translations);
    }
    public Optional<List<ProductDTO>> addLanguageDetailsToProduct(List<ProductDTO> productDTOS, String languageId){
        LanguageDAO languageDAO = new LanguageDAO();
        List<ProductDTO> productDTOList = languageDAO.addLanguageProperties(productDTOS,languageId).get()
                .stream()
                .filter(Objects::nonNull)
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
