package com.example.firstrestapi.DTOs;

import com.mysql.cj.util.StringUtils;

import java.util.Objects;
import java.util.UUID;

public class Rating {
    String headline;
    String description;
    float rating;
    Integer userId;
    String ratingId;
    public Rating(String headline, String description, float rating, Integer userId, String ratingId) {
        this.headline = headline;
        this.description = description;
        this.rating = rating;
        this.userId = userId;
        this.ratingId = ratingId;
    }

    public String getHeadline() {
        return headline;
    }

    public String getDescription() {
        return description;
    }

    public float getRating() {
        return rating;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getRatingId() {
        return ratingId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void generateRatingId() {
        if(StringUtils.isNullOrEmpty(this.ratingId)){
            this.ratingId = UUID.randomUUID().toString();
        }
    }
}
