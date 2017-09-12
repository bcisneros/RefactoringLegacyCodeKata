package org.devdelicias.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PatientTest {

    private static final Date NOT_EXPIRES = null;
    private static final DrugIngredient ZINC = new DrugIngredient(1L, "Zinc", NOT_EXPIRES);
    private static final Allergy ALLERGY_TO_ZINC = new Allergy(ZINC.id());
    private static final DrugIngredient OTHER_INGREDIENT = new DrugIngredient(2L, "Any Other Ingredient", NOT_EXPIRES);
    private Patient patient;

    @Before
    public void setUp() throws Exception {
        patient = new Patient(1L, "John");
        patient.add(ALLERGY_TO_ZINC);
    }

    @Test
    public void inform_when_has_allergy_to_an_ingredient() {
        assertThat(patient.hasAllergyTo(ZINC), is(true));
    }

    @Test
    public void informs_when_has_not_allergy_to_given_ingredient() {
        assertThat(patient.hasAllergyTo(OTHER_INGREDIENT), is(false));
    }
}