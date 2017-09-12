package org.devdelicias.model;

public class Allergy {
    private final Long _ingredientId;

    public Allergy(Long ingredientId) {
        this._ingredientId = ingredientId;
    }

    private Long ingredientId() {
        return _ingredientId;
    }

    boolean isProducedBy(DrugIngredient ingredient) {
        return ingredientId().equals(ingredient.id());
    }
}
