package com.example.firstrestapi.dao;


import com.example.firstrestapi.dto.Product;

import com.example.firstrestapi.Database.mongo.ExtendedProductInfoModel;
import com.example.firstrestapi.dto.Rating;
import com.example.firstrestapi.dto.RatingContext;
import com.example.firstrestapi.Database.mongo.ProductRepository;
import com.example.firstrestapi.util.ErrorCode;
import com.mysql.cj.util.StringUtils;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ProductMongoDAO {
    private static final Logger log = LoggerFactory.getLogger(ProductMongoDAO.class);
    ProductRepository productRepository;
    @Autowired
    public ProductMongoDAO(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void injectExtendedProductInfoIntoProduct(Product product, @Nullable Integer userId) {
        var info = getExtendedProductInfoByProductId(product.getId());

        if(info.isEmpty()){
            return;
        }
        product.setImages(info.get().getImages());
        var ratings = info.get().getRatings();
        if(Objects.nonNull(userId)){
            var userRating = ratings.stream().filter(rating -> rating.getUserId().equals(userId)).findFirst();
            userRating.ifPresent(ratings::remove);
            userRating.ifPresent(rating -> ratings.add(0,rating));
        }
        product.setRatings(ratings);
    }

    public List<Rating> getRatingsByProductId(int productId) {
        var info = getExtendedProductInfoByProductId(productId);
        return info.map(ExtendedProductInfoModel::getRatings).orElseGet(List::of);

    }

    public ErrorCode injectRatingIntoExtendedProductInfo(RatingContext context){
        var info = getExtendedProductInfoByProductId(context.productId());
        ExtendedProductInfoModel eInfo;
        if(info.isEmpty()) {
            eInfo = new ExtendedProductInfoModel("", context.productId(), null, null);
            eInfo.addRating(context.rating());
            productRepository.save(eInfo);
            return ErrorCode.NONE;
        }
        eInfo = info.get();

        List<Integer> usersThatRatedTheProductAlready = eInfo.getRatings()
                .stream()
                .map(Rating::getUserId)
                .toList();
        boolean hasUserAlreadyRated = usersThatRatedTheProductAlready.contains(context.rating().getUserId());
        if(!hasUserAlreadyRated){
            // The Rating has no id yet so it has to be generated
            context.rating().generateRatingId();
            eInfo.addRating(context.rating());
            productRepository.save(eInfo);
            return ErrorCode.NONE;
        }

        // Update Rating
        if(!StringUtils.isNullOrEmpty(context.rating().getRatingId())){
            log.info("User {} is updating his rating for product={}", context.rating().getUserId(), context.productId());
            eInfo.removeRating(context.rating().getRatingId(), context.rating().getUserId());
            eInfo.addRating(context.rating());
            productRepository.save(eInfo);
            return ErrorCode.NONE;
        }
        // Already Rated
        log.warn("User {} has already rated this product={}, unable to add rating.", context.rating().getUserId(), context.productId());
        return ErrorCode.PRODUCT_ALREADY_RATED;

    }

    public boolean removeRatingFromExtendedProductInfo(int productId, int uId, String ratingId){
        var info = getExtendedProductInfoByProductId(productId);
        if(info.isEmpty()){
            log.warn("Could not remove rating because it does not exist - ratingId={}", ratingId);
            return false;
        }

        log.info("Removed rating for productId={}, ratingId={}", productId, ratingId);
        if(!info.get().removeRating(ratingId, uId)){
            log.warn("Could not remove rating for productId={}, ratingId={}", productId, ratingId);
            return false;
        }
        productRepository.save(info.get());
        return true;
    }

    private Optional<ExtendedProductInfoModel> getExtendedProductInfoByProductId(int productId) {
        ExtendedProductInfoModel info = this.productRepository.findExtendedProductInfoByProductId(productId);
        if(Objects.isNull(info)) {
            log.warn("Extended product info with id {} not found  in MongoDB", productId);
            return Optional.empty();
        }
        return Optional.of(info);
    }
}
