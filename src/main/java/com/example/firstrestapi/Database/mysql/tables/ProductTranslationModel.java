package com.example.firstrestapi.Database.mysql.tables;

import jakarta.persistence.*;
import lombok.Getter;

@Table(name = "productTranslation")
@Entity
@Getter
public class ProductTranslationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;
    private String languageId;
    private String displayName;

}
