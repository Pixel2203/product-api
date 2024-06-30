package com.example.firstrestapi.DTOs;

import java.util.ArrayList;
import java.util.Arrays;

public class Product extends ProductTeaser {
    private String[] images;
    private Rating[] ratings;
    public Product(int id, String teaserImage, int categoryId) {
        super(id, teaserImage, categoryId);
    }

    public static Product of(ProductTeaser productTeaser) {
        Product product = new Product(productTeaser.getId(), productTeaser.getTeaserImage(), productTeaser.getCategoryId());
        product.setDetails(productTeaser.getDetails());
        product.setDisplayName(productTeaser.getDisplayName());
        product.setDisplayPrice(productTeaser.getDisplayPrice());
        return product;
    }



    public String[] getImages() {
        return images;
    }

    public Rating[] getRatings() {
        return ratings;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
    public void setRatings(Rating[] ratings) {
        this.ratings = ratings;
        setRatingAverage();
    }
    private void setRatingAverage() {
       if(ratings == null) { return; }
       float average = Arrays.stream(ratings)
               .map(Rating::rating)
               .reduce(0f, Float::sum);
       this.ratingAverage = average / ratings.length;
    }
}
