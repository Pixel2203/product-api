package com.example.firstrestapi.Database.mysql.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Table(name = "detailTranslation")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailTranslationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull private Integer productId;
    @NotNull private String languageId;
    @NotNull private String name;
    @NotNull @Column(name = "translated_value") private String value;
}
