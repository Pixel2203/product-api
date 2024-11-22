package com.example.firstrestapi.Database.mysql.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "products")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BaseProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String imageUrl;
    private Integer categoryId;
    private Float price;

}
