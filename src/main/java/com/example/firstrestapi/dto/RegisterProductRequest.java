package com.example.firstrestapi.dto;

import lombok.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
@Builder
public record RegisterProductRequest(@NotNull String img, @NotNull String categoryIdName, @NotNull Float price,@NotNull List<ProductLanguageTranslation> translations) {

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[img: ").append(this.img).append(" ,");
        stringBuilder.append("categoryId: ").append(this.categoryIdName).append(" ,");
        stringBuilder.append("price: ").append(price).append(",");
        stringBuilder.append("translations: {");
        for(ProductLanguageTranslation translation : translations){
            stringBuilder.append("languageId: ").append(translation.languageId()).append(",");
            stringBuilder.append("displayName: ").append(translation.displayName()).append(",");

            stringBuilder.append("details: [");
            for(ProductDetail productDetail : translation.details()){
                stringBuilder.append("{" + "detailName: ").append(productDetail.displayName()).append(", displayValue: ").append(productDetail.displayValue()).append("}");
            }
            stringBuilder.append("]");

        }
        stringBuilder.append("}");
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }
}
