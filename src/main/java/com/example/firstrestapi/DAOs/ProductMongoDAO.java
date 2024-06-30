package com.example.firstrestapi.DAOs;


import com.example.firstrestapi.DTOs.Product;
import com.example.firstrestapi.DTOs.Rating;
import com.example.firstrestapi.Database.ExtendedProductInfo;
import com.example.firstrestapi.Database.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public void injectExtendedProductInfoIntoProduct(Product product) {
        ExtendedProductInfo info = this.productRepository.findExtendedProductInfoByProductId(product.getId());

        if(Objects.isNull(info)) {
            log.warn("Extended product info with id {} not found  in MongoDB", product.getId());
            return;
        }
        product.setImages(info.getImages());
        product.setRatings(info.getRatings());
    }

    public Optional<Rating[]> getRatingsByProductId(int productId) {
        ExtendedProductInfo info = this.productRepository.findExtendedProductInfoByProductId(productId);
        if(Objects.isNull(info)) {
            log.warn("Extended product info with id {} not found  in MongoDB", productId);
            return Optional.empty();
        }
        Rating[] ratings = info.getRatings();
        return Optional.of(ratings);

    }
}
