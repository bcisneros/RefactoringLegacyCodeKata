package org.devdelicias.service;

import java.util.Date;
import java.util.List;
import org.devdelicias.model.Allergy;
import org.devdelicias.model.Drug;
import org.devdelicias.model.DrugIngredient;
import org.devdelicias.model.Patient;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DispenseDrugServiceTest {

    private static final long ANY_DRUG_ID = 6L;
    private static final String ANY_DRUG_NAME = "Any Drug Name";
    private static final long ANY_INGREDIENT_ID = 63L;
    private static final long ANY_PATIENT_ID = 56L;
    private DispenseDrugService service = new TestableDispenseDrugService();
    private static final Patient UNUSED_PATIENT = null;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private Date currentDate;
    private boolean orderCreated = false;

    @Test
    public void cantDispenseDrugIfThereAreNotIngredients() throws DispenseDrugException {
        String drugName = "Lipitor";
        Drug drugWithoutIngredients = new Drug(ANY_DRUG_ID, drugName);

        cantDispenseBecause("There are not ingredients for drug: Lipitor");

        service.dispenseDrugToPatient(drugWithoutIngredients, UNUSED_PATIENT);
    }

    @Test
    public void cantDispenseDrugIfAnyIngredientIsExpired() throws DispenseDrugException {
        currentDate = toDate("2018-05-10");
        Date yesterday = toDate("2018-05-09");
        String ingredientName = "Vitamin A";
        Drug drug = new Drug(ANY_DRUG_ID, ANY_DRUG_NAME);
        DrugIngredient expiredIngredient = new DrugIngredient(
            ANY_INGREDIENT_ID, ingredientName, yesterday);
        drug.add(expiredIngredient);

        cantDispenseBecause("Ingredient Vitamin A is expired.");

        service.dispenseDrugToPatient(drug, UNUSED_PATIENT);
    }

    @Test
    public void cantDispenseDrugIfProducesAllergyToPatient() throws DispenseDrugException {
        String drugName = "Antibiotic";
        String ingredientName = "Penicillin";
        String patientName = "Bob";
        currentDate = toDate("2018-04-01");
        Date nextMonth = toDate("2018-05-01");

        DrugIngredient penicillin = new DrugIngredient(ANY_INGREDIENT_ID, ingredientName, nextMonth);

        Drug antibiotic = new Drug(ANY_DRUG_ID, drugName);
        antibiotic.add(penicillin);

        Patient patient = new Patient(ANY_PATIENT_ID, patientName);
        patient.add(allergyTo(penicillin));

        cantDispenseBecause("Could not dispense drug Antibiotic cause patient Bob has allergy to Penicillin");

        service.dispenseDrugToPatient(antibiotic, patient);
    }

    @Test
    public void canDispenseDrugToPatientIfDrugIsSafe() throws DispenseDrugException {
        currentDate = toDate("2018-04-01");
        Date nextMonth = toDate("2018-05-01");
        Date nextYear = toDate("2019-04-01");

        Drug safeDrug = new Drug(1L, "Safe Drug");
        DrugIngredient safeIngredient = new DrugIngredient(2L, "Safe Ingredient", nextYear);
        safeDrug.add(safeIngredient);

        Patient patient = new Patient(14L, "Clark Kent");

        DrugIngredient kriptonite = new DrugIngredient(1L, "Kriptonite", nextMonth);
        patient.add(allergyTo(kriptonite));

        service.dispenseDrugToPatient(safeDrug, patient);

        assertThat(
            "Should create a new order",
            orderCreated,
            is(true)
        );
    }

    private Date toDate(String aDate) {
        return LocalDate.parse(aDate).toDate();
    }

    private void cantDispenseBecause(String reason) {
        expectedException.expect(DispenseDrugException.class);
        expectedException.expectMessage(is(reason));
    }

    private Allergy allergyTo(DrugIngredient ingredient) {
        return new Allergy(ingredient.id());
    }

    private class TestableDispenseDrugService extends DispenseDrugService {
        @Override
        protected List<DrugIngredient> ingredientsOf(Drug drug) {
            return drug.ingredients();
        }

        @Override
        protected Date currentDate() {
            return currentDate;
        }

        @Override
        protected void createNewOrder(Drug drug, Patient patient) throws OrderException {
            // Do nothing
            orderCreated = true;
        }
    }
}