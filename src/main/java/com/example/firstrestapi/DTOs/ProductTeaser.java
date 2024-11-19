package com.example.firstrestapi.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ProductTeaser extends BaseProduct {



    private String displayName;
    private String displayPrice;
    private List<ProductDetail> details;
    private String languageModel;
    protected float ratingAverage;
    public ProductTeaser(int id, String teaserImage, int categoryId, float price) {
        super(id, teaserImage, categoryId, price);
        displayName = "";
        displayPrice = "";
        details = new ArrayList<>();
    }
    public ProductTeaser(BaseProduct baseProduct) {
        this(baseProduct.getId(), baseProduct.getTeaserImage(), baseProduct.getCategoryId(), baseProduct.getPrice());
    }


    public void setRatingAverage(List<Rating> ratings) {
        if(ratings == null) { return; }
        float average = ratings.stream()
                .map(Rating::getRating)
                .reduce(0f, Float::sum);
        this.ratingAverage = average / ratings.size();
    }

    public static ProductTeaser copyFrom(ProductTeaser productTeaser){
        ProductTeaser dto = new ProductTeaser(productTeaser.getId(), productTeaser.getTeaserImage(), productTeaser.getCategoryId(), productTeaser.getPrice());
        dto.setDetails(productTeaser.getDetails());
        dto.setDisplayName(productTeaser.getDisplayName());
        dto.setDisplayPrice(productTeaser.getDisplayPrice());
        return dto;
    }
}
