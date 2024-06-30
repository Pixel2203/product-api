package com.example.firstrestapi.Database;

import com.example.firstrestapi.DTOs.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepository extends MongoRepository<ExtendedProductInfo, String> {
    @Query("{productId: ?0}")
    ExtendedProductInfo findExtendedProductInfoByProductId(int productId);
}
