package com.example.firstrestapi.util;

public enum ErrorCode {
    NONE(100),
    PRODUCT_ALREADY_RATED(101);


    private final int code;
    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
