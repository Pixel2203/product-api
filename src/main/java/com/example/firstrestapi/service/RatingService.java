package com.example.firstrestapi.service;

import com.example.firstrestapi.config.CustomMessageSource;
import com.example.firstrestapi.dao.ProductMongoDAO;
import com.example.firstrestapi.dto.RatingContext;
import com.example.firstrestapi.request.HttpRequestContext;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.util.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class RatingService extends CService {

    private final ProductMongoDAO productMongoDAO;
    @Autowired
    public RatingService(ProductMongoDAO productMongoDAO,
                         HttpRequestContext requestContext,
                         CustomMessageSource messageSource) {
        super(requestContext, messageSource);
        this.productMongoDAO = productMongoDAO;
    }

    /**
     * Adds a Rating to a products
     * @param context Context to create the rating from
     * @return Returns a Response if it succeeded or not
     */

    public EventResponse<?> addRatingToProduct(RatingContext context) {
        if(Objects.isNull(requestContext.getUserId())){
            return EventResponse.failed("Missing userId!", ErrorCode.INVALID_REQUEST);
        }

        context.insertUserId(requestContext.getUserId());
        ErrorCode errorCode = productMongoDAO.injectRatingIntoExtendedProductInfo(context);

        if(errorCode != ErrorCode.NONE){
            log.warn("Unable to add Rating to product={} with reason: {}" , context.productId(), errorCode);
            return EventResponse.failed(messageSource.get("rating.add.failed.duplicate", requestContext.getLocale()), errorCode);
        }

        log.info("Added Rating to product={}" , context.productId());
        return EventResponse.withoutResult(true, messageSource.get("rating.add.success",requestContext.getLocale()), errorCode);

    }

    public EventResponse<?> removeRatingFromProduct(String ratingId, int productId) {
        if(Objects.isNull(requestContext.getUserId())){
            return EventResponse.failed("Missing userId!", ErrorCode.INVALID_REQUEST);
        }
        boolean worked = productMongoDAO.removeRatingFromExtendedProductInfo(productId, requestContext.getUserId(), ratingId);
        if(!worked){
            return EventResponse.failed("Unable to remove rating!", ErrorCode.NONE);
        }
        return EventResponse.withoutResult(true, "Successfully removed rating!", ErrorCode.NONE);

    }
}
