package com.example.firstrestapi.DTOs;

import com.mysql.cj.util.StringUtils;

public record RatingContext(Rating rating, int productId) {

  public void insertUserId(int userId) {
    this.rating.setUserId(userId);
  }
  public RatingContext(Rating rating, int productId) {

   this.productId = productId;
   if(StringUtils.isNullOrEmpty(rating.getRatingId())){
       rating.generateRatingId();
   }
   this.rating = rating;
  }
}
