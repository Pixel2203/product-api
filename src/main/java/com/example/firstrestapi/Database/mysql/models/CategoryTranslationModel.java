package com.example.firstrestapi.Database.mysql.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Table(name = "category_translation")
@Entity
@Getter
@Setter
public class CategoryTranslationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull private Integer categoryId;
    @NotNull private String translatedName;
    @NotNull private String languageId;
}
