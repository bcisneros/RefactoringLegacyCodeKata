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
    private OrderService orderService;
    private DrugRepository drugRepository;

    public DispenseDrugService(OrderService orderService, DrugRepository drugRepository) {
        this.orderService = orderService;
        this.drugRepository = drugRepository;
    }

    public void dispenseDrugToPatient(Drug drug, Patient patient) throws DispenseDrugException {
        List<DrugIngredient> drugIngredients = drugRepository.findIngredientsBy(drug.id());

        validateDrugHasIngredients(drug, drugIngredients);

        for (DrugIngredient ingredient : drugIngredients) {

            if (ingredient.isExpiredAt(currentDateTime())) {
                throwDispenseDrugExceptionWithMessage(String.format("Ingredient %s is expired.", ingredient.name()));
            }

            if (patient.hasAllergyTo(ingredient)) {
                throwDispenseDrugExceptionWithMessage(
                        String.format("Could not dispense drug %s cause patient %s has allergy to %s",
                                drug.name(), patient.name(), ingredient.name())
                );
            }
        }

        tryToCreateNewOrder(drug, patient);
    }

    private void validateDrugHasIngredients(Drug drug, List<DrugIngredient> drugIngredients) throws DispenseDrugException {
        if (drugIngredients.isEmpty())
            throwDispenseDrugExceptionWithMessage(String.format("There are not ingredients for drug: %s", drug.name()));
    }

    private void tryToCreateNewOrder(Drug drug, Patient patient) throws DispenseDrugException {
        try {
            logger.info("Trying to create new order.");
            orderService.createNewOrder(drug, patient);
            logger.info("Order created.");
        } catch (OrderException e) {
            throwDispenseDrugExceptionWithMessage(e.getMessage());
        }
    }

    private void throwDispenseDrugExceptionWithMessage(String message) throws DispenseDrugException {
        throw new DispenseDrugException(message);
    }

    Date currentDateTime() {
        return new Date();
    }
}
