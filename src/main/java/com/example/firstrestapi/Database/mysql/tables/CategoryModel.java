package com.example.firstrestapi.Database.mysql.tables;

import jakarta.persistence.*;

@Table(name = "categoryids")
@Entity
public class CategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String category_name;

}
