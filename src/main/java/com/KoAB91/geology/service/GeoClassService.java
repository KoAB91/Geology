package com.KoAB91.geology.service;

import com.KoAB91.geology.dto.GeoClassDTO;
import com.KoAB91.geology.entity.GeologicalClass;
import com.KoAB91.geology.enums.DeleteResponse;
import com.KoAB91.geology.extensions.StreamExtension;
import com.KoAB91.geology.repository.GeologicalClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeoClassService {

    @Autowired
    GeologicalClassRepository geologicalClassRepository;

    public List<GeoClassDTO> get() {
        List<GeologicalClass> geoClasses = StreamExtension.toList(
                geologicalClassRepository.findAll());
        if (geoClasses == null){
            return null;
        }
        List<GeoClassDTO> geoClassesDTO = geoClasses
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return geoClassesDTO;
    }

    public GeoClassDTO getByName(String name) {
        GeologicalClass geoClass = geologicalClassRepository.findByName(name);
        if (geoClass == null) {
            return null;
        }
        return convertToDTO(geoClass);

    }

    public GeoClassDTO getByCode(String name) {
        GeologicalClass geoClass = geologicalClassRepository.findByCode(name);
        if (geoClass == null) {
            return null;
        }
        return convertToDTO(geoClass);
    }

    public GeoClassDTO create(String className, String classCode) {
        GeologicalClass geoClass = geologicalClassRepository.findByName(className);
        if (geoClass != null){
            return convertToDTO(geoClass);
        }
        geoClass = geologicalClassRepository.findByCode(classCode);
        if (geoClass != null){
            return convertToDTO(geoClass);
        }
        geoClass = new GeologicalClass();
        geoClass.setName(className);
        geoClass.setCode(classCode);
        geoClass = geologicalClassRepository.save(geoClass);
        return convertToDTO(geoClass);
    }

    public GeoClassDTO updateName(String name, String newName) {

        GeologicalClass geoClass = geologicalClassRepository.findByName(name);
        if (geoClass == null){
            return null;
        }
        geoClass.setName(newName);
        geologicalClassRepository.save(geoClass);

        return convertToDTO(geoClass);
    }

    public GeoClassDTO updateCode(String code, String newCode) {

        GeologicalClass geoClass = geologicalClassRepository.findByCode(code);
        if (geoClass == null){
            return null;
        }
        geoClass.setCode(newCode);
        geologicalClassRepository.save(geoClass);

        return convertToDTO(geoClass);
    }

    public DeleteResponse delete(String name) {
        GeologicalClass geoClass = geologicalClassRepository.findByName(name);
        if (geoClass == null){
            return DeleteResponse.NOT_FOUND;
        }
        geologicalClassRepository.delete(geoClass);
        return DeleteResponse.OK;
    }

    private GeoClassDTO convertToDTO(GeologicalClass geoClass) {
        GeoClassDTO geoClassDTO = new GeoClassDTO();
        geoClassDTO.setName(geoClass.getName());
        geoClassDTO.setCode(geoClass.getCode());
        return geoClassDTO;
    }
}
