package com.KoAB91.geology.dto;

import java.util.List;

public class SectionDTO {

    private String name;
    private List<GeoClassDTO> geologicalClasses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GeoClassDTO> getGeologicalClasses() {
        return geologicalClasses;
    }

    public void setGeologicalClasses(List<GeoClassDTO> geoClasses) { this.geologicalClasses = geoClasses; }
}

