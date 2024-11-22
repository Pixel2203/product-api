package com.example.firstrestapi.util;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NONE("NONE"),
    PRODUCT_ALREADY_RATED("PRODUCT_ALREADY_RATED"),
    NO_TRANSLATIONS_PROVIDED("NO_TRANSLATIONS_PROVIDED"),
    SQL_ERROR("SQL_ERROR"),
    INVALID_REQUEST("INVALID_REQUEST"),
    INSUFFICIENT_PERMISSIONS("INSUFFICIENT_PERMISSIONS");

    private final String code;
    ErrorCode(String code) {
        this.code = code;
    }

}
