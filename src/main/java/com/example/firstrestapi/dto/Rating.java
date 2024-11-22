package com.example.firstrestapi.dto;

import com.mysql.cj.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Rating {
    String headline;
    String description;
    float rating;
    @Setter
    Integer userId;
    String ratingId;
    public Rating(String headline, String description, float rating, Integer userId, String ratingId) {
        this.headline = headline;
        this.description = description;
        this.rating = rating;
        this.userId = userId;
        this.ratingId = ratingId;
    }

    public void generateRatingId() {
        if(StringUtils.isNullOrEmpty(this.ratingId)){
            this.ratingId = UUID.randomUUID().toString();
        }
    }
}
