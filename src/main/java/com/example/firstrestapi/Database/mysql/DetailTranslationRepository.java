package com.example.firstrestapi.Database.mysql;

import com.example.firstrestapi.Database.mysql.models.DetailTranslationModel;
import org.springframework.data.repository.CrudRepository;

public interface DetailTranslationRepository extends CrudRepository<DetailTranslationModel, Integer> {

}
