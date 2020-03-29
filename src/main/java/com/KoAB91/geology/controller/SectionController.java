package com.KoAB91.geology.controller;

import com.KoAB91.geology.dto.SectionDTO;
import com.KoAB91.geology.dto.request.SectionAddGeoClassRequest;
import com.KoAB91.geology.dto.request.SectionCreateRequest;
import com.KoAB91.geology.dto.request.SectionUpdateRequest;
import com.KoAB91.geology.enums.DeleteResponse;
import com.KoAB91.geology.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sections")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @GetMapping()
    @ResponseBody
    public List<SectionDTO> getAll() {
        return sectionService.get();
    }

    @GetMapping("{name}")
    @ResponseBody
    public ResponseEntity<SectionDTO> get(@PathVariable String name) {
        SectionDTO sectionDTO = sectionService.getByName(name);
        if (sectionDTO == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return new ResponseEntity<>(sectionDTO, HttpStatus.OK);
    }

    @GetMapping("/by-code/{classCode}")
    @ResponseBody
    public ResponseEntity<List<SectionDTO>> getByCode(@PathVariable String classCode) {
        List<SectionDTO> sectionsDTOS = sectionService.getByСlassCode(classCode);
        if (sectionsDTOS == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return new ResponseEntity<>(sectionsDTOS, HttpStatus.OK);
    }

    @PostMapping()
    @ResponseBody
    public SectionDTO create(@RequestBody SectionCreateRequest model) {
        return sectionService.create(model.name);
    }

    @PutMapping("{name}/update-name")
    @ResponseBody
    public ResponseEntity<SectionDTO> updateName(@PathVariable String name, @RequestBody SectionUpdateRequest model) {
        SectionDTO sectionDTO = sectionService.updateName(name, model.newName);
        if (sectionDTO == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return new ResponseEntity<>(sectionDTO, HttpStatus.OK);
    }

    @PostMapping("{name}/add-geo-class")
    @ResponseBody
    public ResponseEntity<SectionDTO> addGeoClass(@PathVariable String name, @RequestBody SectionAddGeoClassRequest model) {
        SectionDTO sectionDTO = sectionService.addGeoClass(name, model.className);
        if (sectionDTO == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return new ResponseEntity<>(sectionDTO, HttpStatus.OK);
    }

    @DeleteMapping("{name}")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable String name) {
        DeleteResponse deleteResponse = sectionService.delete(name);
        if (deleteResponse == DeleteResponse.NOT_FOUND) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity
                .ok()
                .build();
    }
}
