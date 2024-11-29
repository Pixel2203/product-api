package com.example.firstrestapi.service;

import com.example.firstrestapi.Database.mysql.CategoryTranslationRepository;
import com.example.firstrestapi.ResponseObjects.ProductyByCategoryResponse;
import com.example.firstrestapi.dao.CategoryDAO;
import com.example.firstrestapi.dao.LanguageDAO;
import com.example.firstrestapi.dao.ProductDAO;
import com.example.firstrestapi.dao.ProductMongoDAO;
import com.example.firstrestapi.dto.*;
import com.example.firstrestapi.Database.DBManager;
import com.example.firstrestapi.Database.mysql.models.BaseProductModel;
import com.example.firstrestapi.Database.mysql.ProductTranslationRepository;
import com.example.firstrestapi.config.CustomMessageSource;
import com.example.firstrestapi.handler.LanguageHandler;
import com.example.firstrestapi.request.HttpRequestContext;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.util.ErrorCode;
import com.example.firstrestapi.util.PriceHelper;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService extends CService{
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;
    private final ProductMongoDAO productMongoDAO;
    private final LanguageHandler languageHandler;

    @Autowired
    public ProductService(DBManager dbManager,
                          ProductDAO productDAO,
                          CategoryDAO categoryDAO,
                          HttpRequestContext requestContext,
                          ProductMongoDAO productMongoDAO,
                          CustomMessageSource customMessageSource,
                          ProductTranslationRepository productTranslationRepository) {
        super(requestContext, customMessageSource);
        this.languageHandler = new LanguageHandler(new LanguageDAO(productTranslationRepository, dbManager));
        this.productMongoDAO = productMongoDAO;
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;
    }

    /**
     * Searches all products that can be found for the given category in the correct language
     * @param categoryId Category of Products
     * @return Returns all Products found by Category
     */
    public EventResponse<?> getProductsByCategoryAndLanguage(String categoryId){
        List<BaseProductModel> foundProducts = productDAO.getProductsByCategory(categoryId);
        if(foundProducts.isEmpty()){
            return EventResponse.failed("No products found for the matching criteria!", ErrorCode.NONE);
        }
        List<ProductTeaser> productTeasersWithTranslation = languageHandler.getProductsWithFullTranslation(foundProducts, requestContext.getLocale());

        if(productTeasersWithTranslation.isEmpty()){
            return EventResponse.failed("Failed to add language details to products!", ErrorCode.NONE);
        }

        for(ProductTeaser productTeaser : productTeasersWithTranslation){
            var ratings = productMongoDAO.getRatingsByProductId(productTeaser.getId());
            if(ratings.isEmpty()) continue;
            productTeaser.setRatingAverage(ratings);
        }
        String translatedCategory = categoryDAO.translateCategoryIdName(categoryId, requestContext.getLocale());
        var responseBody = new ProductyByCategoryResponse(translatedCategory, productTeasersWithTranslation);
        return new EventResponse<>(true, "Successfully collected products!", responseBody, ErrorCode.NONE);

    }

    /**
     * Registers a new Product
     * @param request RegisterProductRequest
     * @return String containing the result
     */
    public EventResponse<?> addProduct(RegisterProductRequest request) {
        ErrorCode result = productDAO.registerProductToDatabase(request);
        return EventResponse.withoutResult(result == ErrorCode.NONE , "Added new product", result);
    }

    /**
     * Returns all productInformation in Cart format for the given productids with the given language
     * @param productIds List of productIds which will be converted
     * @return List of CartProductDTO
     */
    public EventResponse<?> getProductsByIdsWithFallback(Map<Integer,Integer> productIds) {
        List<BaseProductModel> baseProductsInCart = productDAO.getBaseProductsByIds(productIds.keySet()
                .stream()
                .toList());

        if(baseProductsInCart.isEmpty()){
            return EventResponse.failed("Unable to find products!", ErrorCode.NONE);
        }

        // Adds Details
        var translatedProducts = languageHandler.getProductsWithFullTranslation(baseProductsInCart, requestContext.getLocale());

        if(translatedProducts.isEmpty()){
            log.error("Unable to add product translations for ids {}", productIds.keySet());
            return EventResponse.failed("Unable to find product translations", ErrorCode.NONE);
        }

        List<CartProduct> cartProductsWithoutTotalPrice = combineAmountWithProducts(productIds , translatedProducts);

        // Format DisplayPrice Total
        cartProductsWithoutTotalPrice.forEach(cartProduct -> {
            String languageModel = cartProduct.getProduct().getLanguageModel();
            PriceHelper helper = languageHandler.getPriceHelperByLanguage(languageModel);
            if(Objects.isNull(helper)) helper = PriceHelper.Default();
            cartProduct.formatTotalDisplayPrice(helper);
        });
        return new EventResponse<>(true, "Found them!" , cartProductsWithoutTotalPrice, ErrorCode.NONE);
    }

    /**
     *
     * @param productId ProductId of the desired product
     * @return EventResponse of Product Object
     */
    public EventResponse<?> getProductById(int productId) {
        var baseProduct = productDAO.getBaseProductsByIds(List.of(productId));
        if(baseProduct.isEmpty()){
            log.warn("Unable to find product with id {}", productId);
            return EventResponse.failed("Unable to find product!", ErrorCode.NONE);
        }

        List<ProductTeaser> translatedProduct = languageHandler.getProductsWithFullTranslation(baseProduct, requestContext.getLocale());
        if(translatedProduct.isEmpty()){
            log.error("Unable to translate product with id {}", productId);
            return EventResponse.failed("Unable to translate product!", ErrorCode.NONE);
        }
        Product translatedProductWithoutRatingsAndImages = new Product(translatedProduct.get(0));
        this.productMongoDAO.injectExtendedProductInfoIntoProduct(translatedProductWithoutRatingsAndImages, requestContext.getUserId());
        return new EventResponse<>(true, "Successfully got product", translatedProductWithoutRatingsAndImages, ErrorCode.NONE);

    }


    /**
     * Combines the regular Product Information with the amount of products in cart
     * @param productIdAmountMap productId to amount map <ID,AMOUNT>
     * @param products Product Information which will be combined
     * @return List of CartProductDTO containing amount and productInfo
     */
    @Nonnull
    private List<CartProduct> combineAmountWithProducts(Map<Integer,Integer> productIdAmountMap, List<ProductTeaser> products) {
        return products.stream()
                .filter(productDTO -> productIdAmountMap.containsKey(productDTO.getId()))
                .map(productDTO -> new CartProduct(productDTO, productIdAmountMap.get(productDTO.getId())))
                .toList();
    }


}
