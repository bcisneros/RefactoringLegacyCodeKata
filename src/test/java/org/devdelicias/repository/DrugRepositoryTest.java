package org.devdelicias.repository;

import org.junit.Test;

public class DrugRepositoryTest {

    private static final Long ANY_DRUG_ID = 1L;

    @Test(expected = UnsupportedOperationException.class)
    public void find_drug_ingredients_by_drug_id() {
        new DrugRepository().findIngredientsBy(ANY_DRUG_ID);

    }
}