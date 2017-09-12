package org.devdelicias.model;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Patient {
    private final Long _id;
    private final String _name;
    private final List<Allergy> _allergies;

    public Patient(Long id, String name) {
        _id = id;
        _name = name;
        _allergies = new ArrayList<>();
    }

    public Long id() {
        return _id;
    }

    public String name() {
        return _name;
    }

    public List<Allergy> allergies() {
        return _allergies;
    }

    public void add(Allergy... allergies) {
        this._allergies.addAll(asList(allergies));
    }

    public boolean hasAllergyTo(DrugIngredient ingredient) {
        boolean hasAllergy = false;
        List<Allergy> patientAllergies = allergies();
        for (Allergy allergy : patientAllergies) {
            // If patient has allergy to the ingredient throw an exception
            hasAllergy = allergy.ingredientId().equals(ingredient.id());

            if (hasAllergy) {
                break;
            }
        }
        return hasAllergy;
    }
}
