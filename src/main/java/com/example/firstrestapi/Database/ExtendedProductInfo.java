package com.example.firstrestapi.Database;

import com.example.firstrestapi.DTOs.Rating;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("products")
public class ExtendedProductInfo {
    @Id
    private String id;
    private int productId;
    private String[] images;
    private Rating[] ratings;

    public String getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public String[] getImages() {
        return images;
    }

    public Rating[] getRatings() {
        return ratings;
    }

    public ExtendedProductInfo(String id, int productId, String[] images, Rating[] ratings) {
        this.id = id;
        this.images = images;
        this.ratings = ratings;
        this.productId = productId;
    }
}
