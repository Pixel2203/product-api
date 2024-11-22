package com.example.firstrestapi.dto;

import java.util.List;

public record ProductLanguageTranslation(String languageId, String displayName, List<ProductDetail> details) {
}
