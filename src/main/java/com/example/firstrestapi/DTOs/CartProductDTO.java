package com.example.firstrestapi.DTOs;

import java.util.Optional;

public record CartProductDTO(ProductDTO product, int amount, float totalPrice) {
}
