package com.example.firstrestapi.DTOs;

public record RatingContext(Rating rating, int productId) {

  public void insertUserId(int userId) {
    this.rating.setUserId(userId);
  }
  public RatingContext(Rating rating, int productId) {
   this.rating = rating;
   this.productId = productId;
   this.rating.generateRatingId();
  }
}
