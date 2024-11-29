package com.example.firstrestapi.service;

import com.example.firstrestapi.ResponseObjects.ProductyByCategoryResponse;
import com.example.firstrestapi.TestData;
import com.example.firstrestapi.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceTests {
    @Autowired
    private ProductService productService;

    private static Integer registeredProductId;



    @Test
    @Order(1)
    public void registerTestProduct() {

        List<ProductLanguageTranslation> translations = List.of(
                TestData.ProductLanguageTranslations.SAMPLE_TRANSLATION_EN);

        RegisterProductRequest request = RegisterProductRequest.builder()
                .img("https://get-it-easy.de/wp-content/uploads/2022/06/macbook-pro-m1-2020-mieten-get-it-easy.jpg")
                .price(1600.00f)
                .categoryIdName(TestData.Category.LAPTOP)
                .translations(translations)
                .build();

        var response = productService.addProduct(request);
        assert response.success();

    }

    @Test
    @Order(2)
    public void getTestProduct() {
        productService.getRequestContext().setLocale("en");
        var foundProducts = productService.getProductsByCategoryAndLanguage(TestData.Category.LAPTOP);
        assert foundProducts.success();
        var response = (ProductyByCategoryResponse) foundProducts.result();
        assert Objects.nonNull(response);
        List<ProductTeaser> products = response.products();


        var product = products.get(0);
        assert product.getPrice() == 1600.00f;
        registeredProductId = product.getId();
    }

    @Test
    @Order(3)
    public void testTestProductDetailsTranslationFallback() {
        productService.getRequestContext().setLocale("de");
        var requestedProduct =  productService.getProductById(registeredProductId);
        assert requestedProduct.success();
        var foundProduct = (Product) requestedProduct.result();
        assert Objects.nonNull(foundProduct);
        List<ProductDetail> translatedProductDetails = foundProduct.getDetails();

        assert translatedProductDetails.equals(TestData.ProductLanguageTranslations.SAMPLE_TRANSLATION_EN.details());
    }

}
