package com.example.firstrestapi.handler;

import com.example.firstrestapi.dto.ProductDetail;
import com.example.firstrestapi.dto.ProductTeaser;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DetailHandler {


    public void injectTranslatedDetailsIntoProduct(ProductTeaser productTeaser, List<ProductDetail> details) {
        if(details.isEmpty()) { return; }
        details.sort((detail1, detail2) -> {
            int length1 = detail1.displayName().length() + detail1.displayValue().length();
            int length2 = detail2.displayName().length() + detail2.displayValue().length();
            return Integer.compare(length1, length2);
        });
        productTeaser.setDetails(details);

    }





}
