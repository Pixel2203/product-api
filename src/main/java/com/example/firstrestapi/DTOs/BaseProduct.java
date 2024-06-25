package com.example.firstrestapi.DTOs;

public class BaseProduct {
    protected final int id;
    protected final String teaserImage;
    protected final int categoryId;


    public BaseProduct(int id, String teaserImage, int categoryId) {
        this.id = id;
        this.teaserImage = teaserImage;
        this.categoryId = categoryId;
    }

    public String getTeaserImage() {
        return teaserImage;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
