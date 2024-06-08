package com.example.firstrestapi.controller;

import com.example.firstrestapi.Records.*;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.service.LanguageService;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
public class LanguageController {

    private final LanguageService languageService;
    @Autowired
    public LanguageController(LanguageService languageService){
        this.languageService = languageService;
    }

    @GetMapping("/categories")
    public EventResponse<List<Category>> getCategoryByLanguageId(@RequestParam String languageId){
        return languageService.getCategoryEventResponseByLanguageId(languageId);
    }
    @PostMapping("/addCategories")
    public String addCategoriesFromLanguageId(@RequestBody AddCategoriesRequest request){
        if(request.categories().size() == 0){
            return "List must have at least one category!";
        }
        if(StringUtils.isNullOrEmpty(request.languageId())){
            return "Could not find LanguageId!";
        }
        return languageService.addCategoriesByLanguageId(request);
    }

    @GetMapping("/Header")
    @CrossOrigin(origins = "http://192.168.178.19:3000,http://localhost:3000")
    public EventResponse<HeaderTranslationPack> getHeaderTranslation(@RequestParam String languageId){
        return languageService.getHeaderTranslation(languageId);
    }

}
