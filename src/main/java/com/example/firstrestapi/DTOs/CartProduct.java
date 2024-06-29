package com.example.firstrestapi.DTOs;

import com.example.firstrestapi.util.PriceHelper;

public class CartProduct {
    private final ProductTeaser product;
    private final int amount;
    private float totalPrice;
    private String totalDisplayPrice;
    public ProductTeaser getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public CartProduct(ProductTeaser product, int amount, float totalPrice){
        this.product = product;
        this.amount = amount;
        this.totalPrice = totalPrice;
    }
    public CartProduct(ProductTeaser product , int amount){
        this.product = product;
        this.amount = amount;
        calcTotalPrice();
    }

    private void calcTotalPrice() {
        this.totalPrice = product.getPrice() * amount;
    }
    public void formatTotalDisplayPrice(PriceHelper helper){
        this.totalDisplayPrice = helper.buildPrice(totalPrice);
    }
    public String getTotalDisplayPrice(){
        return totalDisplayPrice;
    }

}
