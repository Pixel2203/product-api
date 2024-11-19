package com.example.firstrestapi.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseProduct {
    private final int id;
    private final String teaserImage;
    private final int categoryId;
    private final Float price;
}
