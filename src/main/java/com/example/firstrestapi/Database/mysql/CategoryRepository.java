package com.example.firstrestapi.Database.mysql;

import com.example.firstrestapi.Database.mysql.models.CategoryModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryModel, Integer> {
    Optional<CategoryModel> findCategoryModelByName(String category_name);
    @NotNull List<CategoryModel> findAll();
}
