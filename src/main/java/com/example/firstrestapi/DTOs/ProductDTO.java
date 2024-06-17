package com.example.firstrestapi.DTOs;

import com.example.firstrestapi.Records.ProductDetail;

import java.util.ArrayList;
import java.util.List;

public class ProductDTO  {
    private final int id;
    private final String img;
    private final int categoryId;
    private String displayName;
    private String displayPrice;
    private List<ProductDetail> details;
    private float priceNormal;
    public ProductDTO(int id, String img, int categoryId) {
        this.id = id;
        this.img = img;
        this.categoryId = categoryId;
        displayName = "";
        displayPrice = "";
        details = new ArrayList<>();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDisplayPrice(String displayPrice) {
        this.displayPrice = displayPrice;
    }
    public void setPriceNormal(float priceNormal) { this.priceNormal = priceNormal; }

    public void setDetails(List<ProductDetail> details) {
        this.details = details;
    }

    public List<ProductDetail> getDetails() {
        return details;
    }

    public String getImg() {
        return img;
    }

    public String getDisplayPrice() {
        return displayPrice;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public static ProductDTO copyFrom(ProductDTO productDTO){
        ProductDTO dto = new ProductDTO(productDTO.getId(), productDTO.getImg(), productDTO.getCategoryId());
        dto.setDetails(productDTO.getDetails());
        dto.setDisplayName(productDTO.getDisplayName());
        dto.setDisplayPrice(productDTO.getDisplayPrice());
        return dto;
    }
}
