package com.example.firstrestapi.Database.mysql.tables;

import jakarta.persistence.*;

@Table(name = "detailTranslation")
@Entity
public class DetailTranslationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String productId;
    private String languageId;
    private String name;
    private String value;
}
