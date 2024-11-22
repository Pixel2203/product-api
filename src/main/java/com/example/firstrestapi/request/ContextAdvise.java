package com.example.firstrestapi.request;

import com.example.firstrestapi.CHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;

@ControllerAdvice
@RequiredArgsConstructor
public class ContextAdvise {
    private final HttpRequestContext requestContext;

    @ModelAttribute
    public void populateRequestContext(
            @RequestHeader(value = CHeaders.LOCALE, defaultValue = "de") String locale,
            @RequestHeader(value = CHeaders.USERID, required = false) Integer userId) {
        requestContext.setLocale(locale);
        requestContext.setUserId(userId);
    }
}
