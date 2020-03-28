package com.KoAB91.geology.repository;

import com.KoAB91.geology.entity.GeologicalClass;
import org.springframework.data.repository.CrudRepository;


public interface GeologicalClassRepository extends CrudRepository<GeologicalClass, Integer> {
    GeologicalClass findByName(String name);

    GeologicalClass findByCode(String code);
}

