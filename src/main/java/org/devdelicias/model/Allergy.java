package org.devdelicias.model;

public class Allergy {
    private final Long _ingredientId;

    public Allergy(Long ingredientId) {
        this._ingredientId = ingredientId;
    }

    public Long ingredientId() {
        return _ingredientId;
    }
}
