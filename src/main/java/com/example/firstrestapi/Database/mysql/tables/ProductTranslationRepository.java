package com.example.firstrestapi.Database.mysql.tables;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductTranslationRepository extends CrudRepository<ProductTranslationModel, Integer> {
    Optional<ProductTranslationModel> findProductTranslationModelByProductIdAndLanguageId(Integer productId, String  languageId);
}
