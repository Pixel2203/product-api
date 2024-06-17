package com.example.firstrestapi.service;
import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Records.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LanguageService {

    public static final String fallbackLanguage = "EN";
    private final DetailService detailService;
    @Autowired
    public LanguageService(DetailService detailService) {
        this.detailService = detailService;
    }

    public Optional<List<ProductDTO>> addLanguageDetailsToProduct(List<ProductDTO> productDTOS, String languageId){
        LanguageDAO languageDAO = new LanguageDAO();

        List<ProductDTO> withProductTranslation = languageDAO.addProductTranslation(productDTOS,languageId,true);
        withProductTranslation.forEach(detailService::checkDetailTranslation);
        detailService.addTranslatedDetailsWithFallback(withProductTranslation,languageId,true);
        List<ProductDTO> productDTOList = withProductTranslation
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


}
