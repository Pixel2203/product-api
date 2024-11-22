package com.example.firstrestapi.dto;

import org.jetbrains.annotations.NotNull;

public record TranslateCategoryRequest(
        @NotNull String languageId,
        @NotNull String categoryId,
        @NotNull String translation
) {
}
