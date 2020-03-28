package com.KoAB91.geology.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "sections_geological_classes",
            joinColumns = @JoinColumn(name = "section_id"),
            inverseJoinColumns = @JoinColumn(name = "geological_classes_id"))
    private List<GeologicalClass> geologicalClasses;

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

    public List<GeologicalClass> getGeologicalClasses() {
        return geologicalClasses;
    }

    public void setGeologicalClasses(List<GeologicalClass> geologicalClasses) {
        this.geologicalClasses = geologicalClasses;
    }
}
