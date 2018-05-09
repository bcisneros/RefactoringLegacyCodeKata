package org.devdelicias.service;

import java.util.Date;
import java.util.List;
import org.devdelicias.model.Drug;
import org.devdelicias.model.DrugIngredient;
import org.devdelicias.model.Patient;
import org.joda.time.LocalDate;
import org.junit.Test;

public class DispenseDrugServiceTest {

    private static final Patient UNUSED_PATIENT = null;

    @Test(expected = DispenseDrugException.class)
    public void cantDispenseDrugIfThereAreNotIngredients() throws DispenseDrugException {
        DispenseDrugService service = new TestableDispenseDrugService();
        Drug drug = new Drug(1L, "Drug Name");

        service.dispenseDrugToPatient(drug, UNUSED_PATIENT);
    }

    @Test(expected = DispenseDrugException.class)
    public void cantDispenseDrugIfAnyIngredientIsExpired() throws DispenseDrugException {
        DispenseDrugService service = new TestableDispenseDrugService();
        Drug drug = new Drug(1L, "Drug Name");
        Date yesterday = LocalDate.parse("2018-05-07").toDate();
        DrugIngredient expiredIngredient = new DrugIngredient(1L, "Expired Ing.", yesterday);
        drug.add(expiredIngredient);
        Patient patient = null;

        service.dispenseDrugToPatient(drug, patient);
    }

    private class TestableDispenseDrugService extends DispenseDrugService {
        @Override
        protected List<DrugIngredient> ingredientsOf(Drug drug) {
            return drug.ingredients();
        }
    }
}