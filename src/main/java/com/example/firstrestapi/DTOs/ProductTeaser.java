package com.example.firstrestapi.DTOs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductTeaser extends BaseProduct {



    private String displayName;
    private String displayPrice;
    private List<ProductDetail> details;
    private String languageModel;
    protected float ratingAverage;
    private float price;
    public ProductTeaser(int id, String teaserImage, int categoryId) {
        super(id, teaserImage, categoryId);
        displayName = "";
        displayPrice = "";
        details = new ArrayList<>();
    }
    public ProductTeaser(BaseProduct baseProduct) {
        this(baseProduct.getId(), baseProduct.getTeaserImage(), baseProduct.getCategoryId());
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDisplayPrice(String displayPrice) {
        this.displayPrice = displayPrice;
    }

    public void setLanguageModel(String languageModel) {
        this.languageModel = languageModel;
    }
    public String getLanguageModel() {
        return languageModel;
    }

    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }

    public void setDetails(List<ProductDetail> details) {
        this.details = details;
    }

    public List<ProductDetail> getDetails() {
        return details;
    }



    public String getDisplayPrice() {
        return displayPrice;
    }

    public String getDisplayName() {
        return displayName;
    }

    public float getRatingAverage() {
        return ratingAverage;
    }
    public void setRatingAverage(List<Rating> ratings) {
        if(ratings == null) { return; }
        float average = ratings.stream()
                .map(Rating::getRating)
                .reduce(0f, Float::sum);
        this.ratingAverage = average / ratings.size();
    }

    public static ProductTeaser copyFrom(ProductTeaser productTeaser){
        ProductTeaser dto = new ProductTeaser(productTeaser.getId(), productTeaser.getTeaserImage(), productTeaser.getCategoryId());
        dto.setDetails(productTeaser.getDetails());
        dto.setDisplayName(productTeaser.getDisplayName());
        dto.setDisplayPrice(productTeaser.getDisplayPrice());
        return dto;
    }
}
