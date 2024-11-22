package com.example.firstrestapi.Database.mongo;

import com.example.firstrestapi.dto.Rating;
import com.mysql.cj.util.StringUtils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Document("products")
public class ExtendedProductInfoModel {
    private static final Logger log = LoggerFactory.getLogger(ExtendedProductInfoModel.class);
    @Id
    private String id;
    private final int productId;
    @Nullable
    private final List<String> images;
    @Nullable
    private List<Rating> ratings;


    @Nonnull
    public List<Rating> getRatings() {
        if(Objects.isNull(ratings)) {
            return List.of();
        }
        return ratings;
    }

    public ExtendedProductInfoModel(String id, int productId, List<String> images, List<Rating> ratings) {
        this.id = id;
        if(Objects.isNull(images)) {
            this.images = new ArrayList<>();
        }else {
            this.images = images;
        }

        if(Objects.isNull(ratings)) {
            this.ratings = new ArrayList<>();
        }else {
            this.ratings = ratings;
        }

        this.productId = productId;
    }

    public boolean addRating(Rating rating) {
        if(Objects.isNull(rating)){
            log.warn("Cannot add null rating - productId={}", productId);
            return false;
        }
        if(Objects.isNull(ratings)){
            log.warn("Cannot add rating to list of null - productId={}", productId);
            return false;
        }
        this.ratings.add(rating);
        return true;
    }
    public boolean removeRating(String ratingId, int uId) {
        if(StringUtils.isNullOrEmpty(ratingId)){
            log.warn("Cannot remove null rating - productId={}", productId);
            return false;
        }
        if(Objects.isNull(ratings)){
            log.warn("Cannot remove rating from list of null - productId={}", productId);
            return false;
        }
        var toRemoveRating = ratings.stream().filter(rating -> Objects.nonNull(rating.getRatingId())).filter(rating -> rating.getRatingId().equalsIgnoreCase(ratingId)).findFirst();
        if(toRemoveRating.isEmpty()){
            log.warn("Cannot find rating with id {} - productId={}", ratingId, productId);
            return false;
        }
        boolean isSameUser = toRemoveRating.get().getUserId() == uId;
        if(!isSameUser){
            log.warn("User {} is not permitted to remove this rating={}", uId, ratingId);
            return false;
        }

        return ratings.remove(toRemoveRating.get());

    }

}
