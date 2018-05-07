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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
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

import java.util.Date;
import java.util.List;
import org.devdelicias.model.Allergy;
import org.devdelicias.model.Drug;
import org.devdelicias.model.Ingredient;
import org.devdelicias.model.Patient;
import org.devdelicias.repository.DrugRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class DispenseDrugService.
 *
 * @since 1.0
 */
public class DispenseDrugService {

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Validates if is possible to dispense a Drug to a Patient.
     *
     * @param drug The drug to be dispensed
     * @param patient The patient to dispense the drug
     * @throws DispenseDrugException If drug is has not ingredients, is expired
     *  or contains ingredients that are un-safe to patient. Also if the order
     *  service fails to process the order.
     */
    public final void dispenseDrugToPatient(
        final Drug drug, final Patient patient)
        throws DispenseDrugException {
        final List<Ingredient> ingredients =
            DrugRepository.findIngredientsOf(drug.identifier());
        if (ingredients.size() > 0) {
            for (final Ingredient ingredient : ingredients) {
                final Date today = new Date();
                final Date expiration = ingredient.expirationDate();
                if (expiration.before(today)) {
                    throw new DispenseDrugException(
                        "Ingredient " + ingredient.fullName() + " is expired."
                    );
                } else {
                    List<Allergy> allergies = patient.allAllergies();
                    for (Allergy allergy : allergies) {
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
            try {
                this.logger.info("Trying to create new order.");
                OrderService.createOrder(drug, patient);
                this.logger.info("Order created.");
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
