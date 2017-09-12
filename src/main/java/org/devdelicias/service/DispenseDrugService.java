package org.devdelicias.service;

import org.devdelicias.model.Drug;
import org.devdelicias.model.DrugIngredient;
import org.devdelicias.model.Patient;
import org.devdelicias.repository.DrugRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class DispenseDrugService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void dispenseDrugToPatient(Drug drug, Patient patient) throws DispenseDrugException {

        List<DrugIngredient> drugIngredients = findIngredientsOf(drug);

        validateAreIngredientsForGivenDrug(drug, drugIngredients);

        for (DrugIngredient ingredient : drugIngredients) {

            if (ingredient.isExpiredAt(currentDateTime())) {
                throwDispenseDrugExceptionWithMessage("Ingredient " + ingredient.name() + " is expired.");
            }

            if (patient.hasAllergyTo(ingredient)) {
                throwDispenseDrugExceptionWithMessage("Could not dispense drug " + drug.name() + " cause patient "
                        + patient.name() + " has allergy to " + ingredient.name());
            }
        }

        tryToCreateNewOrder(drug, patient);
    }

    private void validateAreIngredientsForGivenDrug(Drug drug, List<DrugIngredient> drugIngredients) throws DispenseDrugException {
        if (drugIngredients.isEmpty())
            throwDispenseDrugExceptionWithMessage("There are not ingredients for drug: " + drug.name());
    }

    private void tryToCreateNewOrder(Drug drug, Patient patient) throws DispenseDrugException {
        try {
            logger.info("Trying to create new order.");
            createOrder(drug, patient);
            logger.info("Order created.");
        } catch (OrderException e) {
            throwDispenseDrugExceptionWithMessage(e.getMessage());
        }
    }

    private void throwDispenseDrugExceptionWithMessage(String message) throws DispenseDrugException {
        throw new DispenseDrugException(message);
    }

    protected void createOrder(Drug drug, Patient patient) throws OrderException {
        OrderService.createOrder(drug, patient);
    }

    protected Date currentDateTime() {
        return new Date();
    }

    protected List<DrugIngredient> findIngredientsOf(Drug givenDrug) {
        return DrugRepository.findIngredientsOf(givenDrug.id());
    }
}
