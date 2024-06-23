package com.example.firstrestapi.service;

import com.example.firstrestapi.DAOs.ProductDAO;
import com.example.firstrestapi.DTOs.CartProductDTO;
import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Database.DBManager;
import com.example.firstrestapi.Records.RegisterProductRequest;
import com.example.firstrestapi.responses.EventResponse;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private LanguageService languageService;
    public static DBManager dbManager;
    @Autowired
    public ProductService(LanguageService languageService, DBManager dbManager) {
        this.languageService = languageService;
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
        Optional<List<ProductDTO>> optionalProducts = productDAO.getProductsByCategory(categoryId);
        if(optionalProducts.isEmpty()){
            return EventResponse.failed("Unable to get products by category and language!");
        }
        Optional<List<ProductDTO>> optionalProductsWithLanguage = languageService.getProductsWithFullTranslation(optionalProducts.get(), languageId);
        if(optionalProductsWithLanguage.isPresent()){
            return new EventResponse<>(true, "Successfully collected products!", optionalProductsWithLanguage.get());
        }
        return EventResponse.failed("Failed to add language details to products!");
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
        Optional<List<ProductDTO>> productsInCartWithoutTranslation = productDAO.getProductsByIdsWithoutDetails(productIds.keySet()
                .stream()
                .toList());

        if(productsInCartWithoutTranslation.isEmpty()){
            return EventResponse.failed("Unable to find products!");
        }
        // Adds Details
        var translatedProducts = languageService.getProductsWithFullTranslation(productsInCartWithoutTranslation.get(), language);

        if(translatedProducts.isEmpty()){
            log.error("Unable to add product translations for ids {}", productIds.keySet());
            return EventResponse.failed("Unable to find product translations");
        }

        List<CartProductDTO> cartProductsWithoutTotalPrice = combineAmountWithProducts(productIds , translatedProducts.get());
        return new EventResponse<>(true, "Found them!" , cartProductsWithoutTotalPrice);
    }

    /**
     * Combines the regular Product Information with the amount of products in cart
     * @param productIdAmountMap productId to amount map <ID,AMOUNT>
     * @param products Product Information which will be combined
     * @return List of CartProductDTO containing amount and productInfo
     */
    @Nonnull
    private List<CartProductDTO> combineAmountWithProducts(Map<Integer,Integer> productIdAmountMap, List<ProductDTO> products) {
        return products.stream()
                .filter(productDTO -> productIdAmountMap.containsKey(productDTO.getId()))
                .map(productDTO -> new CartProductDTO(productDTO, productIdAmountMap.get(productDTO.getId())))
                .toList();
    }
}
