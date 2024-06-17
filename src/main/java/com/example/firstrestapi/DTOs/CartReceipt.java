package com.example.firstrestapi.DTOs;

public record CartReceipt(CartProductDTO[] products, float totalPrice) {
}
