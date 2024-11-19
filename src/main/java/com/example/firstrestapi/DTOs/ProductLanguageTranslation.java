package com.example.firstrestapi.DTOs;

import java.util.List;

public record ProductLanguageTranslation(String languageId, String displayName, List<ProductDetail> details) {
}
