package com.example.firstrestapi.service;

import com.example.firstrestapi.config.CustomMessageSource;
import com.example.firstrestapi.request.HttpRequestContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class CService {
    protected final HttpRequestContext requestContext;
    protected final CustomMessageSource messageSource;
}
