package com.example.firstrestapi.service;

import com.example.firstrestapi.DAOs.ProductDAO;
import com.example.firstrestapi.DTOs.CartProductDTO;
import com.example.firstrestapi.DTOs.ProductDTO;
import com.example.firstrestapi.Records.RegisterProductRequest;
import com.example.firstrestapi.responses.EventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    private LanguageService languageService;
    @Autowired

    public ProductService(LanguageService languageService) {
        this.languageService = languageService;
    }

    public EventResponse<?> getProductsByCategoryAndLanguage(String categoryId, String languageId){
        ProductDAO productDAO = new ProductDAO();
        Optional<List<ProductDTO>> optionalProducts = productDAO.getProductsByCategory(categoryId);
        if(optionalProducts.isEmpty()){
            return EventResponse.failed("Unable to get products by category and language!");
        }
        Optional<List<ProductDTO>> optionalProductsWithLanguage = languageService.addLanguageDetailsToProduct(optionalProducts.get(), languageId);
        if(optionalProductsWithLanguage.isPresent()){
            return new EventResponse<>(true, "Successfully collected products!", optionalProductsWithLanguage.get());
        }
        return EventResponse.failed("Failed to add language details to products!");
    }

    public Optional<List<CartProductDTO>> getProductsByIds(Map<Integer,Integer> idAmountMap, String languageId){
        ProductDAO productDAO = new ProductDAO();
        Optional<Map<Integer, CartProductDTO>> optionalProducts = productDAO.getProductsByIds(idAmountMap);
        if(optionalProducts.isEmpty()){
            return Optional.empty();
        }
        List<ProductDTO> products = optionalProducts.get().values().stream().map(CartProductDTO::product).toList();
        Optional<List<ProductDTO>> optionalProductsWithLanguage = languageService.addLanguageDetailsToProduct(products, languageId);
        if(optionalProductsWithLanguage.isEmpty()){
            return Optional.empty();
        }
        Map<Integer, CartProductDTO> foundProducts = new HashMap<>();
        optionalProductsWithLanguage.get()
                                    .stream()
                                    .map(productDTO -> new CartProductDTO(productDTO, optionalProducts.get().get(productDTO.getId()).amount()   , null ))
                                    .forEach(cartProductDTO -> foundProducts.put(cartProductDTO.product().getId(), cartProductDTO));

        return Optional.of(foundProducts.values().stream().toList());
    }

    public Optional<String> registerProduct(RegisterProductRequest request) {
        return new ProductDAO().registerProductToDatabase(request);
        //return Optional.of(request.toString());

    }

    public EventResponse<?> getDetailsForProductsByIds(List<Integer> productIds) {
        ProductDAO dao = new ProductDAO();
        return null;
    }
}
