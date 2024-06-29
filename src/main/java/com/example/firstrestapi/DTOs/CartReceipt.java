package com.example.firstrestapi.DTOs;

public record CartReceipt(CartProduct[] products, float totalPrice) {
}
