package com.example.firstrestapi.Database.mysql.tables;

import jakarta.persistence.*;

@Table(name = "products")
@Entity
public class BaseProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String imageUrl;
    private Integer categoryId;
    private Float price;

}
