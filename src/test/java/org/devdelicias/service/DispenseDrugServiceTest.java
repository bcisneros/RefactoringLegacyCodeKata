package org.devdelicias.service;

import org.devdelicias.model.Allergy;
import org.devdelicias.model.Drug;
import org.devdelicias.model.DrugIngredient;
import org.devdelicias.model.Patient;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class DispenseDrugServiceTest {

    private static final List<DrugIngredient> NO_INGREDIENTS = Collections.emptyList();
    private static final Date CURRENT_DATE = LocalDateTime.parse("2017-09-12T07:40").toDate();
    private static final Date FUTURE_DATE = LocalDateTime.parse("2017-09-12T08:40").toDate();
    private static final Date PAST_DATE = LocalDateTime.parse("2017-08-12T09:00").toDate();
    private static final DrugIngredient PENICILLIN = new DrugIngredient(156L, "Penicillin", FUTURE_DATE);
    private static final Allergy ALLERGY_TO_PENICILLIN = new Allergy(PENICILLIN.id());

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    DispenseDrugService dispenseDrugService = Mockito.spy(new DispenseDrugService());

    @Before
    public void setUp() throws Exception {
        Mockito.doReturn(CURRENT_DATE).when(dispenseDrugService).currentDateTime();
    }

    @Test
    public void validate_a_drug_should_have_ingredients() throws DispenseDrugException {
        Drug lipitor = new Drug(1L, "Lipitor");
        ingredientForDrugAre(lipitor, NO_INGREDIENTS);
        Patient bob = new Patient(4L, "Bob");
        expectDispenseDrugExceptionWithMessage("There are not ingredients for drug: Lipitor");

        dispenseDrugService.dispenseDrugToPatient(lipitor, bob);
    }

    @Test
    public void validate_drug_ingredients_are_not_expired() throws DispenseDrugException {
        Drug xanax = new Drug(2L, "Xanax");
        Patient richard = new Patient(5L, "Richard");
        expectDispenseDrugExceptionWithMessage("Ingredient Expired Ingredient is expired.");
        ingredientForDrugAre(xanax, ingredientListContaining(
                new DrugIngredient(1L, "Not Expired Ingredient", FUTURE_DATE),
                new DrugIngredient(2L, "Expired Ingredient", PAST_DATE)
        ));

        dispenseDrugService.dispenseDrugToPatient(xanax, richard);
    }

    /**
     * US #123 Check if the patient has allergy to any ingredient of the drug
     */
    @Test
    public void validate_drug_ingredient_does_not_produce_allergies_to_patient() throws DispenseDrugException {
        Drug antibiotic = new Drug(3L, "Antibiotic");
        Patient adam = new Patient(6L, "Adam");
        adam.add(ALLERGY_TO_PENICILLIN);

        ingredientForDrugAre(antibiotic, ingredientListContaining(
                new DrugIngredient(1L, "Not Expired Ingredient", FUTURE_DATE),
                PENICILLIN
        ));
        expectDispenseDrugExceptionWithMessage("Could not dispense drug Antibiotic cause patient Adam has allergy to Penicillin");

        dispenseDrugService.dispenseDrugToPatient(antibiotic, adam);
    }

    @Test
    public void create_new_order_when_drug_is_ok() throws DispenseDrugException, OrderException {
        Drug validDrug = new Drug(12L, "Any Drug");
        Patient anyPatient = new Patient(9L, "Juan");
        ingredientForDrugAre(validDrug, ingredientListContaining(
                new DrugIngredient(1L, "Not Expired Ingredient", FUTURE_DATE),
                new DrugIngredient(2L, "Other Ingredient", FUTURE_DATE)));
        Mockito.doNothing().when(dispenseDrugService).createOrder(validDrug, anyPatient);

        dispenseDrugService.dispenseDrugToPatient(validDrug, anyPatient);

        Mockito.verify(dispenseDrugService).createOrder(validDrug, anyPatient);
    }

    @Test
    public void inform_when_new_order_fails() throws OrderException, DispenseDrugException {
        Drug validDrug = new Drug(12L, "Any Drug");
        Patient anyPatient = new Patient(9L, "Juan");
        ingredientForDrugAre(validDrug, ingredientListContaining(
                new DrugIngredient(1L, "Not Expired Ingredient", FUTURE_DATE)
        ));
        Mockito.doThrow(new OrderException("Order Exception Message")).when(dispenseDrugService).createOrder(validDrug, anyPatient);
        expectDispenseDrugExceptionWithMessage("Order Exception Message");

        dispenseDrugService.dispenseDrugToPatient(validDrug, anyPatient);
    }

    private void ingredientForDrugAre(Drug xanax, List<DrugIngredient> ingredients) {
        Mockito.doReturn(
                ingredients
        ).when(dispenseDrugService).findIngredientsOf(xanax);
    }

    private void expectDispenseDrugExceptionWithMessage(String message) {
        expectedException.expect(DispenseDrugException.class);
        expectedException.expectMessage(is(message));
    }

    private List<DrugIngredient> ingredientListContaining(DrugIngredient... ingredients) {
        return Arrays.asList(ingredients);
    }
}