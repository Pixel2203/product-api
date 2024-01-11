package com.example.firstrestapi.Records;

import java.util.List;

public record AddCategoriesRequest(String languageId, List<Category> categories) {
}
