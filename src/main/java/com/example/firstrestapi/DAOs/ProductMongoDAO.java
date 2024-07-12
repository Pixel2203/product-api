package com.example.firstrestapi.DAOs;


import com.example.firstrestapi.DTOs.Product;

import com.example.firstrestapi.DTOs.ExtendedProductInfo;
import com.example.firstrestapi.DTOs.Rating;
import com.example.firstrestapi.DTOs.RatingContext;
import com.example.firstrestapi.Database.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductMongoDAO {
    private static final Logger log = LoggerFactory.getLogger(ProductMongoDAO.class);
    ProductRepository productRepository;
    @Autowired
    public ProductMongoDAO(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void injectExtendedProductInfoIntoProduct(Product product) {
        var info = getExtendedProductInfoByProductId(product.getId());

        if(info.isEmpty()){
            return;
        }
        product.setImages(info.get().getImages());
        product.setRatings(info.get().getRatings());
    }

    public List<Rating> getRatingsByProductId(int productId) {
        var info = getExtendedProductInfoByProductId(productId);
        return info.map(ExtendedProductInfo::getRatings).orElseGet(List::of);

    }

    public void injectRatingIntoExtendedProductInfo(RatingContext context){
        var info = getExtendedProductInfoByProductId(context.productId());
        ExtendedProductInfo eInfo;
        if(info.isEmpty()) {
            eInfo = new ExtendedProductInfo("", context.productId(), null, null);
            eInfo.addRating(context.rating());
            productRepository.save(eInfo);
            return;
        }
        eInfo = info.get();
        eInfo.addRating(context.rating());
        productRepository.save(eInfo);
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

    private Optional<ExtendedProductInfo> getExtendedProductInfoByProductId(int productId) {
        ExtendedProductInfo info = this.productRepository.findExtendedProductInfoByProductId(productId);
        if(Objects.isNull(info)) {
            log.warn("Extended product info with id {} not found  in MongoDB", productId);
            return Optional.empty();
        }
        return Optional.of(info);
    }
}
