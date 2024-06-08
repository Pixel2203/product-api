package com.example.firstrestapi.service;
import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DTOs.CartProductDTO;
import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Records.*;
import com.example.firstrestapi.responses.EventResponse;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class LanguageService {

    public static final String fallbackLanguage = "EN";

    public EventResponse<List<Category>> getCategoryEventResponseByLanguageId(String languageId){
        Optional<List<Category>> foundCategoryTranslations = getCategoriesByLanguageIdWithFallback(languageId);
        return foundCategoryTranslations
                .map(categories -> new EventResponse<>(true, "Found Categories!", categories))
                .orElseGet(() -> new EventResponse<>(false, "Could not retrieve category translations!", null));
    }

    private Optional<List<Category>> getCategoriesByLanguageIdWithFallback(String languageId){
        Optional<HashMap<Integer,Category>> fallbackCategoriesOptional = new LanguageDAO().getCategoryTranslationByLanguageId(fallbackLanguage);
        Optional<HashMap<Integer,Category>> foundCategoriesOptional = new LanguageDAO().getCategoryTranslationByLanguageId(languageId);

        if(fallbackCategoriesOptional.isEmpty()){
            return Optional.empty();
        }
        HashMap<Integer,Category> fallbackCategories = fallbackCategoriesOptional.get();
        HashMap<Integer,Category> foundCategoriesByLanguage = foundCategoriesOptional.orElseGet(HashMap::new);

        List<Category> missingCategoryTranslations = fallbackCategories.keySet()
                .stream()
                .filter(categoryId -> Objects.isNull(foundCategoriesByLanguage.get(categoryId))).map(fallbackCategories::get).toList();
        List<Category> categoryTranslationsWithFallback = new ArrayList<>(foundCategoriesByLanguage.values());
        categoryTranslationsWithFallback.addAll(missingCategoryTranslations);
        return Optional.of(categoryTranslationsWithFallback);
    }

    private Optional<List<LanguageObject>> getLanguagesByLanguageIdWithFallback(String languageId){
        Optional<HashMap<String,LanguageObject>> foundLanguageTranslations = new LanguageDAO().getLanguagesById(languageId);
        if(foundLanguageTranslations.isEmpty()){
            return Optional.empty();
        }

        Optional<HashMap<String,LanguageObject>> foundFallbackTranslations = new LanguageDAO().getLanguagesById(fallbackLanguage);
        if(foundFallbackTranslations.isEmpty()){
            return Optional.empty();
        }

        HashMap<String,LanguageObject> fallback = foundFallbackTranslations.get();
        HashMap<String,LanguageObject> foundTranslations = foundLanguageTranslations.get();

        List<LanguageObject> missingLanguages = fallback.values().stream()
                .map(LanguageObject::langId)
                .filter(Predicate.not(foundTranslations::containsKey))
                .map(fallback::get)
                .toList();

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

    public EventResponse<HeaderTranslationPack> getHeaderTranslation(String languageId){

        // Category translations
        Optional<List<Category>> foundCategoriesOptional = getCategoriesByLanguageIdWithFallback(languageId);
        if(foundCategoriesOptional.isEmpty()){
            return new EventResponse<>(false, "Unable to fetch categories!", null);
        }
        // Language Translation (German --> Deutsch , Spanish --> Spanisch)
        Optional<List<LanguageObject>> languages = getLanguagesByLanguageIdWithFallback(languageId);
        if(languages.isEmpty()){
            return new EventResponse<>(false, "Unable to fetch LanguageTranslations", null);
        }

        HeaderLangData headerLangData = new HeaderLangData(foundCategoriesOptional.get(),languages.get());
        return new EventResponse<>(
                true,
                "Successfully retrieved languagePack!",
                new HeaderTranslationPack(languageId,headerLangData));
    }

}
