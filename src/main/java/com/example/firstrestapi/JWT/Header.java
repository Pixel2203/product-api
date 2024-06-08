package com.example.firstrestapi.JWT;

public record Header(String alg, String typ) {
    public static Header Default() {
        return new Header("SHA-256", "JWT");
    }
}
