package com.example.firstrestapi.dto;

import org.jetbrains.annotations.NotNull;

public record ProductDetail(@NotNull String displayName, @NotNull String displayValue) {
}
