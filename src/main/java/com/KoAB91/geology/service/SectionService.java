package com.KoAB91.geology.service;

import com.KoAB91.geology.dto.GeoClassDTO;
import com.KoAB91.geology.dto.SectionDTO;
import com.KoAB91.geology.entity.GeologicalClass;
import com.KoAB91.geology.entity.Section;
import com.KoAB91.geology.extensions.StreamExtension;
import com.KoAB91.geology.repository.GeologicalClassRepository;
import com.KoAB91.geology.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {
    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    GeologicalClassRepository geologicalClassRepository;

    public List<SectionDTO> get() {
        List<Section> sections = StreamExtension.toList(
                sectionRepository.findAll());

        List<SectionDTO> sectionsDTO = sections
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return sectionsDTO;
    }

    public SectionDTO getByName(String name) {
        Section section = sectionRepository.findByName(name);
        return convertToDTO(section);
    }

    public List<SectionDTO> getByСlassCode(String classCode) {
        GeologicalClass geoClass = geologicalClassRepository.findByCode(classCode);
        List<SectionDTO> sectionsDTO = geoClass.getSections()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return sectionsDTO;
    }


    public SectionDTO create(String name) {
        Section section = new Section();
        section.setName(name);
        section = sectionRepository.save(section);
        return convertToDTO(section);
    }

    public SectionDTO updateName(String name, String newName) {
        Section section = sectionRepository.findByName(name);
        section.setName(newName);
        section = sectionRepository.save(section);
        return convertToDTO(section);
    }

    public SectionDTO addGeoClass(String name, String className) {
        Section section = sectionRepository.findByName(name);
        GeologicalClass geoClass = geologicalClassRepository.findByName(className);
        section.getGeologicalClasses().add(geoClass);
        section = sectionRepository.save(section);
        return convertToDTO(section);
    }

    public void delete(String name) {
        Section section = sectionRepository.findByName(name);
        sectionRepository.delete(section);
    }

    private SectionDTO convertToDTO(Section section) {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setName(section.getName());

        List<GeologicalClass> geoClasses = section.getGeologicalClasses();
        if (geoClasses != null && !geoClasses.isEmpty()) {
            List<GeoClassDTO> geoClassDTOs = new ArrayList<>();
            for (GeologicalClass geoClass : geoClasses) {
                GeoClassDTO geoClassDTO = new GeoClassDTO();
                geoClassDTO.setName(geoClass.getName());
                geoClassDTO.setCode(geoClass.getCode());
                geoClassDTOs.add(geoClassDTO);
            }

            sectionDTO.setGeologicalClasses(geoClassDTOs);
        }

        return sectionDTO;
    }
}
