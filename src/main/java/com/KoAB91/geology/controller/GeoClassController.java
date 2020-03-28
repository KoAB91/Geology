package com.KoAB91.geology.controller;

import com.KoAB91.geology.dto.GeoClassDTO;
import com.KoAB91.geology.dto.request.GeoClassCreateRequest;
import com.KoAB91.geology.dto.request.GeoClassUpdateCodeRequest;
import com.KoAB91.geology.dto.request.GeoClassUpdateNameRequest;
import com.KoAB91.geology.service.GeoClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/geo-classes")
public class GeoClassController {


    @Autowired
    private GeoClassService geoClassService;

    @GetMapping()
    @ResponseBody
    public List<GeoClassDTO> getAll() {
        return geoClassService.get();
    }

    @GetMapping("/name/{name}")
    @ResponseBody
    public GeoClassDTO getByName(@PathVariable String name) {
        return geoClassService.getByName(name);
    }

    @GetMapping("/code/{code}")
    @ResponseBody
    public GeoClassDTO getByCode(@PathVariable String code) {
        return geoClassService.getByCode(code);
    }

    @PostMapping()
    @ResponseBody
    public GeoClassDTO create(@RequestBody GeoClassCreateRequest model) {
        return geoClassService.create(model.className, model.classCode);
    }

    @PutMapping("{name}/update-name")
    @ResponseBody
    public GeoClassDTO updateName(@PathVariable String name, @RequestBody GeoClassUpdateNameRequest model) {
        return geoClassService.updateName(name, model.classNewName);
    }

    @PutMapping("{code}/update-code")
    @ResponseBody
    public GeoClassDTO updateCode(@PathVariable String code, @RequestBody GeoClassUpdateCodeRequest model) {
        return geoClassService.updateCode(code, model.classNewCode);
    }

    @DeleteMapping("{name}")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable String name) {
        geoClassService.delete(name);
        return ResponseEntity
                .ok()
                .build();
    }
}
