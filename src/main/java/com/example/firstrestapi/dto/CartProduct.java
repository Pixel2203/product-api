package com.example.firstrestapi.dto;

import com.example.firstrestapi.util.PriceHelper;
import lombok.Getter;

public class CartProduct {
    @Getter
    private final ProductTeaser product;

    @Getter
    private final int amount;
    private float totalPrice;
    @Getter
    private String totalDisplayPrice;


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

}
