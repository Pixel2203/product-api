package com.example.firstrestapi.Records;

import java.util.List;

public record RegisterProductRequest(int id, String img, int categoryId, List<ProductLanguageTranslation> translations) {

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[productId: " + this.id + " ,");
        stringBuilder.append("img: " + this.img + " ,");
        stringBuilder.append("categoryId: " + this.categoryId + " ,");
        stringBuilder.append("translations: {");
        for(ProductLanguageTranslation translation : translations){
            stringBuilder.append("languageId: " + translation.languageId() + ",");
            stringBuilder.append("displayName: " + translation.displayName() + ",");
            stringBuilder.append("displayPrice: " + translation.displayPrice() + ",");
            stringBuilder.append("details: [");
            for(ProductDetail productDetail : translation.details()){
                stringBuilder.append("{" +"detailName: " + productDetail.detailName() + ", value: " + productDetail.value()+ "}");
            }
            stringBuilder.append("]");

        }
        stringBuilder.append("}");
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }
}
