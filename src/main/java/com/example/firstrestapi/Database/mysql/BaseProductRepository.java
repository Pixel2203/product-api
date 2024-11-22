package com.example.firstrestapi.Database.mysql;

import com.example.firstrestapi.Database.mysql.models.BaseProductModel;
import org.springframework.data.repository.CrudRepository;

public interface BaseProductRepository extends CrudRepository<BaseProductModel, Integer> {

}
