package com.example.firstrestapi.DTOs;

public class CartProductDTO{
    private final ProductTeaser product;
    private final int amount;
    private float totalPrice;

    public ProductTeaser getProduct() {
        return product;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public int getAmount() {
        return amount;
    }

    public CartProductDTO(ProductTeaser product, int amount, float totalPrice){
        this.product = product;
        this.amount = amount;
        this.totalPrice = totalPrice;
    }
    public CartProductDTO(ProductTeaser product , int amount){
        this.product = product;
        this.amount = amount;
    }


    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
