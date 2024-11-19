package com.example.firstrestapi.service;

import com.example.firstrestapi.DAOs.LanguageDAO;
import com.example.firstrestapi.DAOs.ProductDAO;
import com.example.firstrestapi.DAOs.ProductMongoDAO;
import com.example.firstrestapi.DTOs.*;
import com.example.firstrestapi.Database.DBManager;
import com.example.firstrestapi.Database.mysql.tables.ProductTranslationRepository;
import com.example.firstrestapi.EnvConfig;
import com.example.firstrestapi.handler.LanguageHandler;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.util.ErrorCode;
import com.example.firstrestapi.util.PriceHelper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final LanguageHandler languageHandler;
    public static DBManager dbManager;
    private final ProductMongoDAO productMongoDAO;
    private final ResourceBundleMessageSource messageSource;

    @Autowired
    public ProductService(DBManager dbManager, ProductMongoDAO productMongoDAO, EnvConfig envConfig, ProductTranslationRepository productTranslationRepository) {
        this.languageHandler = new LanguageHandler(new LanguageDAO(productTranslationRepository));
        this.productMongoDAO = productMongoDAO;

        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("translation/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.of(envConfig.getDefaultLanguage()));
        this.messageSource = messageSource;

        ProductService.dbManager = dbManager;
        ProductService.dbManager.connect();
    }

    /**
     * Searches all products that can be found for the given category in the correct language
     * @param categoryId Category of Products
     * @param languageId Language for ProductDetails
     * @return Returns all Products found by Category
     */
    public EventResponse<?> getProductsByCategoryAndLanguage(String categoryId, String languageId){
        ProductDAO productDAO = new ProductDAO();
        Optional<List<BaseProduct>> optionalProducts = productDAO.getProductsByCategory(categoryId);
        if(optionalProducts.isEmpty()){
            return EventResponse.failed("Unable to get products by category and language!", ErrorCode.NONE);
        }
        Optional<List<ProductTeaser>> optionalProductsWithLanguage = languageHandler.getProductsWithFullTranslation(optionalProducts.get(), languageId);

        if(optionalProductsWithLanguage.isEmpty()){
            return EventResponse.failed("Failed to add language details to products!", ErrorCode.NONE);
        }
        var teasers = optionalProductsWithLanguage.get();
        for(ProductTeaser productTeaser : teasers){
            var ratings = productMongoDAO.getRatingsByProductId(productTeaser.getId());
            if(ratings.isEmpty()){
             productTeaser.setRatingAverage(null);
             continue;
            }
            productTeaser.setRatingAverage(ratings);
        }


        return new EventResponse<>(true, "Successfully collected products!", teasers, ErrorCode.NONE.getCode());


    }

    /**
     * Registers a new Product
     * @param request RegisterProductRequest
     * @return String containing the result
     */
    public EventResponse<?> registerProduct(RegisterProductRequest request) {
        return EventResponse.withoutResult(true, new ProductDAO().registerProductToDatabase(request).get(), ErrorCode.NONE);
    }

    /**
     * Returns all productInformation in Cart format for the given productids with the given language
     * @param productIds List of productIds which will be converted
     * @param language Language of Product Details and Name , Price
     * @return List of CartProductDTO
     */
    public EventResponse<?> getProductsByIdsWithFallback(Map<Integer,Integer> productIds, String language) {
        ProductDAO productDAO = new ProductDAO();
        Optional<List<BaseProduct>> baseProductsInCart = productDAO.getBaseProducts(productIds.keySet()
                .stream()
                .toList());

        if(baseProductsInCart.isEmpty()){
            return EventResponse.failed("Unable to find products!", ErrorCode.NONE);
        }

        // Adds Details
        var translatedProducts = languageHandler.getProductsWithFullTranslation(baseProductsInCart.get(), language);

        if(translatedProducts.isEmpty()){
            log.error("Unable to add product translations for ids {}", productIds.keySet());
            return EventResponse.failed("Unable to find product translations", ErrorCode.NONE);
        }

        List<CartProduct> cartProductsWithoutTotalPrice = combineAmountWithProducts(productIds , translatedProducts.get());

        // Format DisplayPrice Total
        cartProductsWithoutTotalPrice.forEach(cartProduct -> {
            String languageModel = cartProduct.getProduct().getLanguageModel();
            PriceHelper helper = languageHandler.getPriceHelperByLanguage(languageModel);
            cartProduct.formatTotalDisplayPrice(helper);
        });
        return new EventResponse<>(true, "Found them!" , cartProductsWithoutTotalPrice, ErrorCode.NONE.getCode());
    }

    /**
     *
     * @param productId ProductId of the desired product
     * @param languageId languageId of the language in which the product will be translated to
     * @param userId Sorts the returned ratings so the rating of this user is the first element in the list
     * @return EventResponse of Product Object
     */
    public EventResponse<?> getProductById(int productId, String languageId, @Nullable Integer userId) {
        ProductDAO productDAO = new ProductDAO();
        var baseProduct = productDAO.getBaseProducts(List.of(productId));
        if(baseProduct.isEmpty()){
            log.warn("Unable to find product with id {}", productId);
            return EventResponse.failed("Unable to find product!", ErrorCode.NONE);
        }

        Optional<List<ProductTeaser>> translatedProduct = languageHandler.getProductsWithFullTranslation(baseProduct.get(), languageId);
        if(translatedProduct.isEmpty() || translatedProduct.get().isEmpty()){
            log.error("Unable to translate product with id {}", productId);
            return EventResponse.failed("Unable to translate product!", ErrorCode.NONE);
        }
        Product translatedProductWithoutRatingsAndImages = Product.of(translatedProduct.get().get(0));
        this.productMongoDAO.injectExtendedProductInfoIntoProduct(translatedProductWithoutRatingsAndImages, userId);
        return new EventResponse<>(true, "Successfully got product", translatedProductWithoutRatingsAndImages, ErrorCode.NONE.getCode());

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

    /**
     * Adds a Rating to a products
     * @param context Context to create the rating from
     * @param uId User that wrote the Rating and sended it to Gateway API
     * @return Returns a Response if it succeeded or not
     */

    public EventResponse<?> addRatingToProduct(RatingContext context, int uId, String language) {
        context.insertUserId(uId);
        ErrorCode errorCode = productMongoDAO.injectRatingIntoExtendedProductInfo(context);
        if(errorCode == ErrorCode.NONE){
            log.info("Added Rating to product={}" , context.productId());
            return EventResponse.withoutResult(true, messageSource.getMessage("rating.add.success",null,Locale.of(language)), errorCode);
        }
        log.warn("Unable to add Rating to product={}" , context.productId());
        return EventResponse.failed(messageSource.getMessage("rating.add.failed.duplicate",null, Locale.of(language)), errorCode);
    }

    public EventResponse<?> removeRatingFromProduct(String ratingId, int uId, int productId) {
        boolean worked = productMongoDAO.removeRatingFromExtendedProductInfo(productId, uId, ratingId);
        if(worked)
            return EventResponse.withoutResult(true, "Successfully removed rating!", ErrorCode.NONE);
        return EventResponse.failed("Unable to remove rating!", ErrorCode.NONE);
    }
}
