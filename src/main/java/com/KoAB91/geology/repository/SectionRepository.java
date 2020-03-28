package com.KoAB91.geology.repository;

import com.KoAB91.geology.entity.Section;
import org.springframework.data.repository.CrudRepository;


public interface SectionRepository extends CrudRepository<Section, Integer> {
    Section findByName(String name);
}
