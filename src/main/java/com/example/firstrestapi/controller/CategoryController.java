package com.example.firstrestapi.controller;

import com.example.firstrestapi.dto.RatingContext;
import com.example.firstrestapi.dto.TranslateCategoryRequest;
import com.example.firstrestapi.permission.AuthorizationToken;
import com.example.firstrestapi.permission.RequiresPermission;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.service.CategoryService;
import com.example.firstrestapi.service.RatingService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category-translation")
    public EventResponse<?> addCategoryTranslation(@RequestBody TranslateCategoryRequest request){
        return categoryService.addCategory(request);
    }

    @GetMapping("/categories")
    public EventResponse<?> getAllCategories(){
        return categoryService.getAllCategories();
    }


}
