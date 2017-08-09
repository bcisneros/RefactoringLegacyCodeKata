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
        List<DrugIngredient> drugIngredients = DrugRepository.findIngredientsOf(drug.getId());

        // If exists ingredients
        if (drugIngredients.size() > 0) {
            // Iterate ingredients for validations
            for (DrugIngredient ingredient : drugIngredients) {

                // Check if the ingredient is expired
                Date today = new Date();
                Date expirationDate = ingredient.getExpirationDate();

                if (expirationDate.before(today)) {
                    throw new DispenseDrugException("Ingredient " + ingredient.getName() + " is expired.");
                } else {
                    // US #123 Check if the patient has allergy to any ingredient of the drug
                    List<Allergy> patientAllergies = patient.getAllergies();
                    for (Allergy allergy : patientAllergies) {
                        // If patient has allergy to the ingredient throw an exception
                        if (allergy.getIngredientId().equals(ingredient.getId())) {
                            throw new DispenseDrugException("Could not dispense drug " + drug.getName() + " cause patient "
                                    + patient.getName() + " has allergy to " + ingredient.getName());
                        }
                    }
                }
            }

            // Try to create new Order
            try {
                logger.info("Trying to create new order.");
                OrderService.createOrder(drug, patient);
                logger.info("Order created.");

            } catch (OrderException e) {
                throw new DispenseDrugException(e.getMessage());
            }

        } else {
            throw new DispenseDrugException("There are not ingredients for drug: " + drug.getName());
        }
    }
}
