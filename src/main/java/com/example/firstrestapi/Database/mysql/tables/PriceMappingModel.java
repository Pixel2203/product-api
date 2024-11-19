package com.example.firstrestapi.Database.mysql.tables;

import jakarta.persistence.*;

@Table(name = "price_mapping")
@Entity
public class PriceMappingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String country;

    private String prefix;
    private String suffix;
    private Boolean use_dot;

}
