package com.example.firstrestapi.Database.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepository extends MongoRepository<ExtendedProductInfoModel, String> {
    @Query("{productId: ?0}")
    ExtendedProductInfoModel findExtendedProductInfoByProductId(int productId);

    @Query("{productId: ?0}")
    void updateExtendedProductInfoByProductIdAnd(int productId, ExtendedProductInfoModel extendedProductInfo);



}
