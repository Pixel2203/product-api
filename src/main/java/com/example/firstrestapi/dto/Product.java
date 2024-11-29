package com.example.firstrestapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter

public class Product extends ProductTeaser {
    @Setter
    private List<String> images;
    private List<Rating> ratings;

    public Product(ProductTeaser productTeaser) {
        super(
                productTeaser.getId(),
                productTeaser.getImageUrl(),
                productTeaser.getCategoryId(),
                productTeaser.getPrice(),
                productTeaser.getDisplayName() ,
                productTeaser.getDisplayPrice(),
                productTeaser.getDetails()
        );
        this.images = new ArrayList<>();
        this.ratings = new ArrayList<>();
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
        setRatingAverage();
    }
    private void setRatingAverage() {
       if(ratings == null) { return; }
       float average = ratings.stream()
               .map(Rating::getRating)
               .reduce(0f, Float::sum);
       this.ratingAverage = average / ratings.size();
    }
}
