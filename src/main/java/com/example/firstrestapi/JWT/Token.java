package com.example.firstrestapi.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public record Token(String email, int sub, Date expire) {
    private static final Map<String , Object> headers = Map.of(
            "alg","SHA-256",
            "typ","JWT"
    );
    @Nullable
    public String buildToken() {
        JWTCreator.Builder tokenBuilder = JWT.create()
                .withExpiresAt(expire)
                .withClaim("email", email)
                .withSubject(String.valueOf(sub))
                .withHeader(headers);
        try {
            return tokenBuilder.sign(Algorithm.HMAC256("MYKEY"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }

    }
}
