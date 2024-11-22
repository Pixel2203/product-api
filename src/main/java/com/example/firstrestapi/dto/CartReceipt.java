package com.example.firstrestapi.dto;

public record CartReceipt(CartProduct[] products, float totalPrice) {
}
