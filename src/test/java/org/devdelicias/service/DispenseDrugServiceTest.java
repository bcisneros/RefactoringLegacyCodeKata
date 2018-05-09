package org.devdelicias.service;

import java.util.List;
import org.devdelicias.model.Drug;
import org.devdelicias.model.DrugIngredient;
import org.devdelicias.model.Patient;
import org.junit.Test;

public class DispenseDrugServiceTest {

    private static final Patient UNUSED_PATIENT = null;

    @Test(expected = DispenseDrugException.class)
    public void cantDispenseDrugIfThereAreNotIngredients() throws DispenseDrugException {
        DispenseDrugService service = new TestableDispenseDrugService();
        Drug drug = new Drug(1L, "Drug Name");

        service.dispenseDrugToPatient(drug, UNUSED_PATIENT);
    }

    private class TestableDispenseDrugService extends DispenseDrugService {
        @Override
        protected List<DrugIngredient> ingredientsOf(Drug drug) {
            return drug.ingredients();
        }
    }
}