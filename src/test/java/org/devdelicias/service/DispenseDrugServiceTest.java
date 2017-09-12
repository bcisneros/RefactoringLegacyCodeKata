package org.devdelicias.service;

import org.devdelicias.model.Allergy;
import org.devdelicias.model.Drug;
import org.devdelicias.model.DrugIngredient;
import org.devdelicias.model.Patient;
import org.devdelicias.repository.DrugRepository;
import org.devdelicias.util.Clock;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DispenseDrugServiceTest {

    private static final List<DrugIngredient> NO_INGREDIENTS = Collections.emptyList();
    private static final Date CURRENT_DATE = LocalDateTime.parse("2017-09-12T07:40").toDate();
    private static final Date FUTURE_DATE = LocalDateTime.parse("2017-09-12T08:40").toDate();
    private static final DrugIngredient OTHER_VALID_INGREDIENT = new DrugIngredient(2L, "Other Ingredient", FUTURE_DATE);
    private static final DrugIngredient A_NOT_EXPIRED_INGREDIENT = new DrugIngredient(1L, "Not Expired Ingredient", FUTURE_DATE);
    private static final Date PAST_DATE = LocalDateTime.parse("2017-08-12T09:00").toDate();
    private static final DrugIngredient AN_EXPIRED_INGREDIENT = new DrugIngredient(2L, "Expired Ingredient", PAST_DATE);
    private static final DrugIngredient PENICILLIN = new DrugIngredient(156L, "Penicillin", FUTURE_DATE);
    private static final Allergy ALLERGY_TO_PENICILLIN = new Allergy(PENICILLIN.id());
    private static final Drug LIPITOR = new Drug(1L, "Lipitor");
    private static final Drug XANAX = new Drug(2L, "Xanax");
    private static final Patient ANY_PATIENT = new Patient(5L, "Richard");
    private static final Patient PATIENT_WITH_ALLERGY = patientWith(ALLERGY_TO_PENICILLIN);
    private static final Drug ANTIBIOTIC = new Drug(3L, "Antibiotic");
    private static final Drug A_DISPENSABLE_DRUG = new Drug(12L, "Any Drug");
    private static final String ORDER_EXCEPTION_MESSAGE = "Order Exception Message";
    private static final long ANY_PATIENT_ID = 6L;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private OrderService orderService = mock(OrderService.class);
    private DrugRepository drugRepository = mock(DrugRepository.class);
    private Clock clock = mock(Clock.class);

    @InjectMocks
    private DispenseDrugService dispenseDrugService = Mockito.spy(new DispenseDrugService(orderService, drugRepository));

    @Before
    public void setUp() throws Exception {
        doReturn(CURRENT_DATE).when(clock).currentDateTime();
    }

    @Test
    public void validate_a_drug_should_have_ingredients() throws DispenseDrugException {
        expectDispenseDrugExceptionWithMessage("There are not ingredients for drug: Lipitor");
        configureDrugIngredients(LIPITOR, NO_INGREDIENTS);

        dispenseDrugService.dispenseDrugToPatient(LIPITOR, null);
    }

    @Test
    public void validate_drug_ingredients_are_not_expired() throws DispenseDrugException {
        expectDispenseDrugExceptionWithMessage("Ingredient Expired Ingredient is expired.");
        configureDrugIngredients(
                XANAX,
                ingredients(
                        A_NOT_EXPIRED_INGREDIENT,
                        AN_EXPIRED_INGREDIENT
                ));

        dispenseDrugService.dispenseDrugToPatient(XANAX, ANY_PATIENT);
    }

    /**
     * US #123 Check if the patient has allergy to any ingredient of the drug
     */
    @Test
    public void validate_drug_ingredient_does_not_produce_allergies_to_patient() throws DispenseDrugException {
        expectDispenseDrugExceptionWithMessage("Could not dispense drug Antibiotic cause patient Adam has allergy to Penicillin");
        configureDrugIngredients(
                ANTIBIOTIC,
                ingredients(
                        A_NOT_EXPIRED_INGREDIENT,
                        PENICILLIN
                ));

        dispenseDrugService.dispenseDrugToPatient(ANTIBIOTIC, PATIENT_WITH_ALLERGY);
    }

    @Test
    public void create_new_order_when_drug_is_ok() throws DispenseDrugException, OrderException {
        configureDrugIngredients(
                A_DISPENSABLE_DRUG,
                ingredients(
                        A_NOT_EXPIRED_INGREDIENT,
                        OTHER_VALID_INGREDIENT
                )
        );

        dispenseDrugService.dispenseDrugToPatient(A_DISPENSABLE_DRUG, ANY_PATIENT);

        verify(orderService).createNewOrder(A_DISPENSABLE_DRUG, ANY_PATIENT);
    }

    @Test
    public void inform_when_new_order_fails() throws OrderException, DispenseDrugException {
        configureDrugIngredients(A_DISPENSABLE_DRUG, ingredients(
                A_NOT_EXPIRED_INGREDIENT
        ));
        doThrow(new OrderException(ORDER_EXCEPTION_MESSAGE)).when(orderService).createNewOrder(A_DISPENSABLE_DRUG, ANY_PATIENT);
        expectDispenseDrugExceptionWithMessage(ORDER_EXCEPTION_MESSAGE);

        dispenseDrugService.dispenseDrugToPatient(A_DISPENSABLE_DRUG, ANY_PATIENT);
    }

    private static Patient patientWith(Allergy allergy) {
        Patient patient = new Patient(ANY_PATIENT_ID, "Adam");
        patient.add(allergy);
        return patient;
    }

    private void configureDrugIngredients(Drug givenDrug, List<DrugIngredient> ingredients) {
        doReturn(ingredients).when(drugRepository).findIngredientsBy(givenDrug.id());
    }

    private void expectDispenseDrugExceptionWithMessage(String message) {
        expectedException.expect(DispenseDrugException.class);
        expectedException.expectMessage(is(message));
    }

    private List<DrugIngredient> ingredients(DrugIngredient... ingredients) {
        return Arrays.asList(ingredients);
    }
}