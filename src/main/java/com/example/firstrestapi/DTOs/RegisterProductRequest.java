package com.example.firstrestapi.DTOs;

import java.util.List;

public record RegisterProductRequest(String img, int categoryId, List<ProductLanguageTranslation> translations) {

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[img: ").append(this.img).append(" ,");
        stringBuilder.append("categoryId: ").append(this.categoryId).append(" ,");
        stringBuilder.append("translations: {");
        for(ProductLanguageTranslation translation : translations){
            stringBuilder.append("languageId: ").append(translation.languageId()).append(",");
            stringBuilder.append("displayName: ").append(translation.displayName()).append(",");
            stringBuilder.append("displayPrice: ").append(translation.displayPrice()).append(",");
            stringBuilder.append("details: [");
            for(ProductDetail productDetail : translation.details()){
                stringBuilder.append("{" + "detailName: ").append(productDetail.displayName()).append(", value: ").append(productDetail.value()).append("}");
            }
            stringBuilder.append("]");

        }
        stringBuilder.append("}");
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }
}
