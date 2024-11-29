package com.example.firstrestapi.controller;

import com.example.firstrestapi.dto.RatingContext;
import com.example.firstrestapi.permission.AuthorizationToken;
import com.example.firstrestapi.permission.Permissions;
import com.example.firstrestapi.permission.RequiresPermission;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;


    @PostMapping("/rate")
    @RequiresPermission(requiredPermission = Permissions.RATE_PRODUCT)
    public EventResponse<?> rateProduct(
            @RequestBody RatingContext context,
            @AuthorizationToken
            @RequestHeader(HttpHeaders.AUTHORIZATION) String ignored){
        return ratingService.addRatingToProduct(context);
    }

    @DeleteMapping("/rate/{productId}")
    @RequiresPermission(requiredPermission = Permissions.REMOVE_RATING)
    public EventResponse<?> deleteRating(@RequestBody String ratingId,
                                         @PathVariable int productId){
        return ratingService.removeRatingFromProduct(ratingId, productId);
    }
}
