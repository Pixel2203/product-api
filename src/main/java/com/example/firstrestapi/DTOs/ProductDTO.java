package com.example.firstrestapi.DTOs;

import com.example.firstrestapi.Records.ProductDetail;

import java.util.ArrayList;
import java.util.List;

public class ProductDTO  {
    private int id;
    private String img;
    private int categoryId;
    private String displayName;
    private String displayPrice;
    private List<ProductDetail> details;
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
}
