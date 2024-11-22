
package com.example.firstrestapi.Database.mysql;

import com.example.firstrestapi.Database.mysql.models.CategoryTranslationModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryTranslationRepository extends CrudRepository<CategoryTranslationModel, Integer> {
    boolean existsCategoryTranslationByCategoryIdAndLanguageId(@NotNull Integer categoryId, @NotNull String languageId);

    Optional<CategoryTranslationModel> findCategoryTranslationModelByCategoryIdAndLanguageId(Integer categoryId, String languageId);
}
