package com.example.firstrestapi.service;

import com.example.firstrestapi.DAOs.ProductDAO;
import com.example.firstrestapi.DAOs.ProductMongoDAO;
import com.example.firstrestapi.DTOs.BaseProduct;
import com.example.firstrestapi.DTOs.CartProduct;
import com.example.firstrestapi.DTOs.Product;
import com.example.firstrestapi.DTOs.ProductTeaser;
import com.example.firstrestapi.Database.DBManager;
import com.example.firstrestapi.DTOs.RegisterProductRequest;
import com.example.firstrestapi.handler.LanguageHandler;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.util.PriceHelper;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final LanguageHandler languageHandler;
    public static DBManager dbManager;
    private final ProductMongoDAO productMongoDAO;

    @Autowired
    public ProductService(DBManager dbManager, ProductMongoDAO productMongoDAO) {
        this.languageHandler = LanguageHandler.getInstance();
        this.productMongoDAO = productMongoDAO;
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
            return EventResponse.failed("Unable to get products by category and language!");
        }
        Optional<List<ProductTeaser>> optionalProductsWithLanguage = languageHandler.getProductsWithFullTranslation(optionalProducts.get(), languageId);

        if(optionalProductsWithLanguage.isEmpty()){
            return EventResponse.failed("Failed to add language details to products!");
        }
        var teasers = optionalProductsWithLanguage.get();
        for(ProductTeaser productTeaser : teasers){
            var ratings = productMongoDAO.getRatingsByProductId(productTeaser.getId());
            if(ratings.isEmpty()){
             productTeaser.setRatingAverage();
            }
        }


        if(optionalProductsWithLanguage.isPresent()){
            return new EventResponse<>(true, "Successfully collected products!", optionalProductsWithLanguage.get());
        }


    }

    /**
     * Registers a new Product
     * @param request RegisterProductRequest
     * @return String containing the result
     */
    public Optional<String> registerProduct(RegisterProductRequest request) {
        return new ProductDAO().registerProductToDatabase(request);
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
            return EventResponse.failed("Unable to find products!");
        }

        // Adds Details
        var translatedProducts = languageHandler.getProductsWithFullTranslation(baseProductsInCart.get(), language);

        if(translatedProducts.isEmpty()){
            log.error("Unable to add product translations for ids {}", productIds.keySet());
            return EventResponse.failed("Unable to find product translations");
        }

        List<CartProduct> cartProductsWithoutTotalPrice = combineAmountWithProducts(productIds , translatedProducts.get());

        // Format DisplayPrice Total
        cartProductsWithoutTotalPrice.forEach(cartProduct -> {
            String languageModel = cartProduct.getProduct().getLanguageModel();
            PriceHelper helper = languageHandler.getPriceHelperByLanguage(languageModel);
            cartProduct.formatTotalDisplayPrice(helper);
        });
        return new EventResponse<>(true, "Found them!" , cartProductsWithoutTotalPrice);
    }

    /**
     *
     * @param productId ProductId of the desired product
     * @param languageId languageId of the language in which the product will be translated to
     * @return EventResponse of Product Object
     */
    public EventResponse<?> getProductById(int productId, String languageId) {
        ProductDAO productDAO = new ProductDAO();
        var baseProduct = productDAO.getBaseProducts(List.of(productId));
        if(baseProduct.isEmpty()){
            log.warn("Unable to find product with id {}", productId);
            return EventResponse.failed("Unable to find product!");
        }

        Optional<List<ProductTeaser>> translatedProduct = languageHandler.getProductsWithFullTranslation(baseProduct.get(), languageId);
        if(translatedProduct.isEmpty() || translatedProduct.get().isEmpty()){
            log.error("Unable to translate product with id {}", productId);
            return EventResponse.failed("Unable to translate product!");
        }
        Product translatedProductWithoutRatingsAndImages = Product.of(translatedProduct.get().get(0));
        this.productMongoDAO.injectExtendedProductInfoIntoProduct(translatedProductWithoutRatingsAndImages);
        return new EventResponse<>(true, "Successfully got product", translatedProductWithoutRatingsAndImages);

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
