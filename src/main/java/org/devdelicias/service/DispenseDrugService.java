package org.devdelicias.service;

import org.devdelicias.model.Allergy;
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

        // Find All ingredients of the drug
        List<DrugIngredient> drugIngredients = findIngredientsOf(drug);

        // If exists ingredients
        if (drugIngredients.size() > 0) {
            // Iterate ingredients for validations
            for (DrugIngredient ingredient : drugIngredients) {

                // Check if the ingredient is expired
                Date today = currentDateTime();
                Date expirationDate = ingredient.expirationDate();

                if (expirationDate.before(today)) {
                    throw new DispenseDrugException("Ingredient " + ingredient.name() + " is expired.");
                } else {
                    // US #123 Check if the patient has allergy to any ingredient of the drug
                    List<Allergy> patientAllergies = patient.allergies();
                    for (Allergy allergy : patientAllergies) {
                        // If patient has allergy to the ingredient throw an exception
                        if (allergy.ingredientId().equals(ingredient.id())) {
                            throw new DispenseDrugException("Could not dispense drug " + drug.name() + " cause patient "
                                    + patient.name() + " has allergy to " + ingredient.name());
                        }
                    }
                }
            }

            // Try to create new Order
            try {
                logger.info("Trying to create new order.");
                createOrder(drug, patient);
                logger.info("Order created.");

            } catch (OrderException e) {
                throw new DispenseDrugException(e.getMessage());
            }

        } else {
            throw new DispenseDrugException("There are not ingredients for drug: " + drug.name());
        }
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
