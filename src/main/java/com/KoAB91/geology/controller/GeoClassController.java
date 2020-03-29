package com.KoAB91.geology.controller;

import com.KoAB91.geology.dto.GeoClassDTO;
import com.KoAB91.geology.dto.request.GeoClassCreateRequest;
import com.KoAB91.geology.dto.request.GeoClassUpdateCodeRequest;
import com.KoAB91.geology.dto.request.GeoClassUpdateNameRequest;
import com.KoAB91.geology.enums.DeleteResponse;
import com.KoAB91.geology.service.GeoClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<GeoClassDTO> getByName(@PathVariable String name) {
        GeoClassDTO geoClassDTO = geoClassService.getByName(name);
        if (geoClassDTO == null){
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return new ResponseEntity<>(geoClassDTO, HttpStatus.OK);
    }

    @GetMapping("/code/{code}")
    @ResponseBody
    public ResponseEntity<GeoClassDTO> getByCode(@PathVariable String code) {
        GeoClassDTO geoClassDTO = geoClassService.getByCode(code);
        if (geoClassDTO == null){
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return new ResponseEntity<>(geoClassDTO, HttpStatus.OK);
    }

    @PostMapping()
    @ResponseBody
    public GeoClassDTO create(@RequestBody GeoClassCreateRequest model) {
        return geoClassService.create(model.className, model.classCode);
    }

    @PutMapping("{name}/update-name")
    @ResponseBody
    public ResponseEntity<GeoClassDTO> updateName(@PathVariable String name, @RequestBody GeoClassUpdateNameRequest model) {
        GeoClassDTO geoClassDTO = geoClassService.updateName(name, model.classNewName);
        if (geoClassDTO == null){
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return new ResponseEntity<>(geoClassDTO, HttpStatus.OK);
    }

    @PutMapping("{code}/update-code")
    @ResponseBody
    public ResponseEntity<GeoClassDTO> updateCode(@PathVariable String code, @RequestBody GeoClassUpdateCodeRequest model) {
        GeoClassDTO geoClassDTO = geoClassService.updateCode(code, model.classNewCode);
        if (geoClassDTO == null){
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return new ResponseEntity<>(geoClassDTO, HttpStatus.OK);
    }

    @DeleteMapping("{name}")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable String name) {
        DeleteResponse deleteResponse = geoClassService.delete(name);
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
