package com.example.firstrestapi.DTOs;

import java.util.List;

public class Product extends ProductTeaser {
    private List<String> images;
    private List<Rating> ratings;
    public Product(int id, String teaserImage, int categoryId, float price) {
        super(id, teaserImage, categoryId, price);
    }

    public static Product of(ProductTeaser productTeaser) {
        Product product = new Product(productTeaser.getId(), productTeaser.getTeaserImage(), productTeaser.getCategoryId(), productTeaser.getPrice());
        product.setDetails(productTeaser.getDetails());
        product.setDisplayName(productTeaser.getDisplayName());
        product.setDisplayPrice(productTeaser.getDisplayPrice());
        return product;
    }



    public List<String> getImages() {
        return images;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setImages(List<String> images) {
        this.images = images;
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
