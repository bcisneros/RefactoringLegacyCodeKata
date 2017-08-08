package org.devdelicias.service;

import org.devdelicias.model.Allergy;
import org.devdelicias.model.Drug;
import org.devdelicias.model.DrugIngredient;
import org.devdelicias.model.Patient;
import org.devdelicias.repository.AllergyRepository;
import org.devdelicias.repository.DrugRepository;

import java.util.Date;
import java.util.List;

public class DispenseDrugService {

    public void dispenseDrugToPatient(Drug drug, Patient patient) throws DispenseDrugException, DrugException {

        // Find All ingredients of the drug
        List<DrugIngredient> drugIngredients = DrugRepository.findDrugAllergiesFor(drug.getId());

        // If exists ingredients
        if (drugIngredients.size() > 0) {
            for (DrugIngredient ingredient : drugIngredients) {
                List<Allergy> patientAllergies = AllergyRepository.findAllergiesFor(patient.getId());
                for (Allergy allergy : patientAllergies) {
                    // If patient has allergy to the ingredient throw an exception
                    if (allergy.getIngredientId().equals(ingredient.getId())) {
                        throw new DispenseDrugException("Could not dispense drug " + drug.getName() + " cause patient " + patient.getName() + " has allergy to " + ingredient.getName());
                    }
                }

                // Check if the ingredient is expired
                Date today = new Date();
                Date expirationDate = ingredient.getExpirationDate();

                if (expirationDate.before(today)) {
                    throw new DrugException("Ingredient " + ingredient.getName() + " is expired");
                } else {
                    try {
                        OrderService.createOrder(drug, patient);
                    } catch (OrderException e) {
                        throw new DrugException(e.getMessage());
                    }
                }

            }
        } else {
            throw new DrugException("Drug Ingredients not found for given drug: " + drug.getName());
        }
    }
}
