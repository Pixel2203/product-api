package com.example.firstrestapi.Records;

import java.util.List;

public record ProductLanguageTranslation(String languageId, String displayName, String displayPrice, List<ProductDetail> details) {
}
