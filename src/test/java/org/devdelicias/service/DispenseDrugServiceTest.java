package org.devdelicias.service;

import java.util.Date;
import java.util.List;
import org.devdelicias.model.Allergy;
import org.devdelicias.model.Drug;
import org.devdelicias.model.DrugIngredient;
import org.devdelicias.model.Patient;
import static org.hamcrest.core.Is.is;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DispenseDrugServiceTest {

    private static final long ANY_DRUG_ID = 6L;
    private static final String ANY_DRUG_NAME = "Any Drug Name";
    private static final long ANY_INGREDIENT_ID = 63L;
    private DispenseDrugService service = new TestableDispenseDrugService();
    private static final Patient UNUSED_PATIENT = null;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private Date currentDate;

    @Test
    public void cantDispenseDrugIfThereAreNotIngredients() throws DispenseDrugException {
        String drugName = "Lipitor";
        Drug drugWithoutIngredients = new Drug(ANY_DRUG_ID, drugName);

        expectedException.expect(DispenseDrugException.class);
        expectedException.expectMessage(is("There are not ingredients for drug: Lipitor"));

        service.dispenseDrugToPatient(drugWithoutIngredients, UNUSED_PATIENT);
    }

    @Test
    public void cantDispenseDrugIfAnyIngredientIsExpired() throws DispenseDrugException {
        currentDate = LocalDate.parse("2018-05-10").toDate();
        Date yesterday = LocalDate.parse("2018-05-09").toDate();
        String ingredientName = "Vitamin A";
        Drug drug = new Drug(ANY_DRUG_ID, ANY_DRUG_NAME);
        DrugIngredient expiredIngredient = new DrugIngredient(
            ANY_INGREDIENT_ID, ingredientName, yesterday);
        drug.add(expiredIngredient);

        expectedException.expect(DispenseDrugException.class);
        expectedException.expectMessage(is("Ingredient Vitamin A is expired."));

        service.dispenseDrugToPatient(drug, UNUSED_PATIENT);
    }

    @Test
    public void cantDispenseDrugIfProducesAllergyToPatient() throws DispenseDrugException {
        Drug antibiotic = new Drug(1L, "Antibiotic");
        currentDate = LocalDate.parse("2018-04-01").toDate();
        Date nextMonth = LocalDate.parse("2018-05-01").toDate();
        DrugIngredient penicillin = new DrugIngredient(25L, "Penicillin", nextMonth);
        antibiotic.add(penicillin);
        Patient patientWithAllergyToPenicillin = new Patient(1L, "Bob");
        patientWithAllergyToPenicillin.add(allergyTo(penicillin));

        expectedException.expect(DispenseDrugException.class);
        expectedException.expectMessage(is("Could not dispense drug Antibiotic cause patient Bob has allergy to Penicillin"));

        service.dispenseDrugToPatient(antibiotic, patientWithAllergyToPenicillin);
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
    }
}