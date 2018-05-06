package org.devdelicias.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Drug {
    private final Long _id;
    private final String _name;
    private List<DrugIngredient> ingredients;

    public Drug(Long id, String name) {
        _id = id;
        _name = name;
        ingredients = new ArrayList<>();
    }

    public Long id() {
        return _id;
    }

    public String name() {
        return _name;
    }

    public void add(DrugIngredient... drugIngredients) {
        this.ingredients.addAll(Arrays.asList(drugIngredients));
    }

    public List<DrugIngredient> ingredients() {
        return Collections.unmodifiableList(this.ingredients);
    }
}
