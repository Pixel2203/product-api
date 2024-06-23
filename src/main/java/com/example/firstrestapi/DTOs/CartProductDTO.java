package com.example.firstrestapi.DTOs;

import java.util.Optional;

public class CartProductDTO{
    private final ProductDTO product;
    private final int amount;
    private float totalPrice;

    public ProductDTO getProduct() {
        return product;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public int getAmount() {
        return amount;
    }

    public CartProductDTO(ProductDTO product, int amount, float totalPrice){
        this.product = product;
        this.amount = amount;
        this.totalPrice = totalPrice;
    }
    public CartProductDTO(ProductDTO product , int amount){
        this.product = product;
        this.amount = amount;
    }


    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
