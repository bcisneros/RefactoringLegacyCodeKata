package org.devdelicias.model;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Patient {
    private Long id;
    private String name;
    private List<Allergy> allergies;

    public Patient() {
        allergies = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Allergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<Allergy> allergies) {
        this.allergies = allergies;
    }

    public void add(Allergy... allergies) {
        this.allergies.addAll(asList(allergies));
    }
}
