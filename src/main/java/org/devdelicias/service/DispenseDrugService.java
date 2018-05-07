/*
 * MIT License
 *
 * Copyright (c) 2018 Developers Delicias
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.devdelicias.service;

import org.devdelicias.model.Allergy;
import org.devdelicias.model.Drug;
import org.devdelicias.model.Ingredient;
import org.devdelicias.model.Patient;
import org.devdelicias.repository.DrugRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Class DispenseDrugService.
 * @author Benjamin Cisneros (cisnerosbarraza@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public class DispenseDrugService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void dispenseDrugToPatient(Drug drug, Patient patient)
        throws DispenseDrugException {

        // Find All ingredients of the drug
        List<Ingredient> ingredients =
            DrugRepository.findIngredientsOf(drug.identifier());

        // If exists ingredients
        if (ingredients.size() > 0) {
            // Iterate ingredients for validations
            for (Ingredient ingredient : ingredients) {

                // Check if the ingredient is expired
                Date today = new Date();
                Date expirationDate = ingredient.expirationDate();

                if (expirationDate.before(today)) {
                    throw new DispenseDrugException(
                        "Ingredient " + ingredient.fullName() + " is expired."
                    );
                } else {
                    // US #123 Check if the patient has allergy to any
                    // ingredient of the drug
                    List<Allergy> patientAllergies = patient.allAllergies();
                    for (Allergy allergy : patientAllergies) {
                        // If patient has allergy to the ingredient
                        // throw an exception
                        if (allergy.ingredient().equals(ingredient)) {
                            throw new DispenseDrugException(
                                "Could not dispense drug " + drug.fullName()
                                    + " cause patient "
                                    + patient.fullName() + " has allergy to "
                                    + ingredient.fullName());
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
            throw new DispenseDrugException(
                "There are not ingredients for drug: " + drug.fullName()
            );
        }
    }
}
