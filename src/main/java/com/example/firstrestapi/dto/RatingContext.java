package com.example.firstrestapi.dto;

public record RatingContext(Rating rating, int productId) {

  public void insertUserId(int userId) {
    this.rating.setUserId(userId);
  }
  public RatingContext(Rating rating, int productId) {

   this.productId = productId;
   this.rating = rating;
  }
}
