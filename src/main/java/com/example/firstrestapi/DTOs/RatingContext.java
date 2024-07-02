package com.example.firstrestapi.DTOs;

public class RatingContext {
  private final Rating rating;
  private final int productId;

  public RatingContext(Rating rating, int productId) {
    this.rating = rating;
    this.productId = productId;
  }
  public Rating getRating() {
    return rating;
  }
  public int getProductId() {
    return productId;
  }
}
