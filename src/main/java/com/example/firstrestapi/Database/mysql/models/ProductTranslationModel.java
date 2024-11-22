package com.example.firstrestapi.Database.mysql.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "productTranslation")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductTranslationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;
    private String languageId;
    private String displayName;

}
