package com.example.firstrestapi.ResponseObjects;

import com.example.firstrestapi.dto.ProductTeaser;

import java.util.List;

public record ProductyByCategoryResponse(String category, List<ProductTeaser> products) {
}
