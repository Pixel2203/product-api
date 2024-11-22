package com.example.firstrestapi.request;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Setter
@Getter
public class HttpRequestContext {

    private String locale;

    @Nullable
    private Integer userId;
}
