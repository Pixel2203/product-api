package com.example.firstrestapi.config;

import com.example.firstrestapi.EnvConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CustomMessageSource {
    private final ResourceBundleMessageSource messageSource;

    @Autowired
    public CustomMessageSource(EnvConfig envConfig) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasename("translation/messages");
        messageSource.setDefaultLocale(Locale.of(envConfig.getDefaultLanguage()));

        this.messageSource = messageSource;
    }


    public String get(String key, String language){
        return this.messageSource.getMessage(key, null ,Locale.of(language));
    }
}
