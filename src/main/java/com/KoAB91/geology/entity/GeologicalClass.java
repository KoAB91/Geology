package com.KoAB91.geology.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "geological_classes")
public class GeologicalClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @ManyToMany(mappedBy = "geologicalClasses")
    private List<Section> sections;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) { this.sections = sections; }

    @PreRemove
    public void deleteGeoCLassFromSections(){
        sections.forEach(section -> section.getGeologicalClasses().remove(this));
    }
}
