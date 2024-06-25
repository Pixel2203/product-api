package com.example.firstrestapi.DTOs;

import com.example.firstrestapi.Records.ProductDetail;

import java.util.ArrayList;
import java.util.List;

public class ProductTeaser extends BaseProduct {



    private String displayName;
    private String displayPrice;
    private List<ProductDetail> details;
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



    public static ProductTeaser copyFrom(ProductTeaser productTeaser){
        ProductTeaser dto = new ProductTeaser(productTeaser.getId(), productTeaser.getTeaserImage(), productTeaser.getCategoryId());
        dto.setDetails(productTeaser.getDetails());
        dto.setDisplayName(productTeaser.getDisplayName());
        dto.setDisplayPrice(productTeaser.getDisplayPrice());
        return dto;
    }
}
