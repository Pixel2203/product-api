package com.example.firstrestapi.util;

import com.mysql.cj.util.StringUtils;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class PriceHelper {
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));
    private String suffix;
    private String prefix;
    private boolean useDot;
    public PriceHelper(String prefix, String suffix, boolean use_dot){
        this.prefix = prefix;
        this.suffix = suffix;
        this.useDot = use_dot;
    }

    @NotNull
    public String buildPrice(float price){
        String displayPrice = String.valueOf(decimalFormat.format(price));
        if(!StringUtils.isNullOrEmpty(prefix)) {
            displayPrice = prefix + displayPrice;
        }
        if(!StringUtils.isNullOrEmpty(suffix)) {
            displayPrice += suffix;
        }
        if(!useDot){
            displayPrice = displayPrice.replaceAll("\\.", ",");
        }
        return displayPrice;
    }
}
