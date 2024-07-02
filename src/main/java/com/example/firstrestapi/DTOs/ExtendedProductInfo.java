package com.example.firstrestapi.DTOs;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document("products")
public class ExtendedProductInfo {
    private static final Logger log = LoggerFactory.getLogger(ExtendedProductInfo.class);
    @Id
    private String id;
    private final int productId;
    @Nullable
    private final List<String> images;
    @Nullable
    private final List<Rating> ratings;

    public String getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public List<String> getImages() {
        return images;
    }

    @Nonnull
    public List<Rating> getRatings() {
        if(Objects.isNull(ratings)) {
            return List.of();
        }
        return ratings;
    }

    public ExtendedProductInfo(String id, int productId, List<String> images, List<Rating> ratings) {
        this.id = id;
        if(Objects.isNull(images)) {
            this.images = new ArrayList<>();
        }else {
            this.images = images;
        }

        if(Objects.isNull(ratings)) {
            this.ratings = new ArrayList<>();
        }else {
            this.ratings = ratings;
        }

        this.productId = productId;
    }

    public boolean addRating(Rating rating) {
        if(Objects.isNull(rating)){
            log.warn("Cannot add null rating - productId={}", productId);
            return false;
        }
        if(Objects.isNull(ratings)){
            log.warn("Cannot add rating to list of null - productId={}", productId);
            return false;
        }
        this.ratings.add(rating);
        return true;
    }

}
