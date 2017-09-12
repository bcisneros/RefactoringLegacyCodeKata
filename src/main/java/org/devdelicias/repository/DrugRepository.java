package org.devdelicias.repository;

import org.devdelicias.model.DrugIngredient;

import java.util.List;

public class DrugRepository {
    public static List<DrugIngredient> findIngredientsOf(Long drugId) {
        throw new UnsupportedOperationException();
    }

    public List<DrugIngredient> findIngredientsBy(Long drugId) {
        return findIngredientsOf(drugId);
    }
}
