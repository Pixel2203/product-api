package com.example.firstrestapi.Database.mysql.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "categoryids")
@Entity
@Getter
@Setter
public class CategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

}
