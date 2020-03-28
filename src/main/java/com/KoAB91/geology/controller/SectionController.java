package com.KoAB91.geology.controller;

import com.KoAB91.geology.dto.SectionDTO;
import com.KoAB91.geology.dto.request.SectionAddGeoClassRequest;
import com.KoAB91.geology.dto.request.SectionCreateRequest;
import com.KoAB91.geology.dto.request.SectionUpdateRequest;
import com.KoAB91.geology.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public SectionDTO get(@PathVariable String name) {
        return sectionService.getByName(name);
    }

    @GetMapping("/by-code/{classCode}")
    @ResponseBody
    public List<SectionDTO> getByCode(@PathVariable String classCode) {
        return sectionService.getByСlassCode(classCode);
    }

    @PostMapping()
    @ResponseBody
    public SectionDTO create(@RequestBody SectionCreateRequest model) {
        return sectionService.create(model.name);
    }

    @PutMapping("{name}/update-name")
    @ResponseBody
    public SectionDTO updateName(@PathVariable String name, @RequestBody SectionUpdateRequest model) {
        return sectionService.updateName(name, model.newName);
    }

    @PostMapping("{name}/add-geo-class")
    @ResponseBody
    public SectionDTO addGeoClass(@PathVariable String name, @RequestBody SectionAddGeoClassRequest model) {
        return sectionService.addGeoClass(name, model.className);
    }

    @DeleteMapping("{name}")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable String name) {
        sectionService.delete(name);
        return ResponseEntity
                .ok()
                .build();
    }
}
