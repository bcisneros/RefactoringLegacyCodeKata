package org.devdelicias.service;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.devdelicias.model.Allergy;
import org.devdelicias.model.Drug;
import org.devdelicias.model.DrugIngredient;
import org.devdelicias.model.Patient;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.devdelicias.model.Drug.DrugBuilder.aDrug;
import static org.devdelicias.model.DrugIngredient.DrugIngredientBuilder.aDrugIngredient;
import static org.devdelicias.model.Patient.PatientBuilder.aPatient;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(JUnitParamsRunner.class)
public class DispenseDrugServiceTest {

	private static final long ANY_DRUG_ID = 6L;
	private static final String ANY_DRUG_NAME = "Any Drug Name";
	private static final long ANY_INGREDIENT_ID = 63L;
	private static final long ANY_PATIENT_ID = 56L;
	private static final String ORDER_EXCEPTION_MESSAGE = "Order Service Failed";
	private DispenseDrugService service = new TestableDispenseDrugService();
	private static final Patient UNUSED_PATIENT = null;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	private Date currentDate;
	private boolean orderCreated = false;
	private OrderException orderException = null;

	@Test
	@Parameters(method = "drugsWithoutIngredients")
	public void cantDispenseDrugIfThereAreNotIngredients(String drugName, String reason) throws DispenseDrugException {
		Drug drugWithoutIngredients = new Drug(ANY_DRUG_ID, drugName);

		cantDispenseBecause(reason);

		service.dispenseDrugToPatient(drugWithoutIngredients, UNUSED_PATIENT);
	}

	@Test
	@Parameters(method = "expiredIngredients")
	public void cantDispenseDrugIfAnyIngredientIsExpired(String ingredientName, String reason) throws DispenseDrugException {
		currentDate = toDate("2018-05-10");
		Date yesterday = toDate("2018-05-09");

		cantDispenseBecause(reason);

		service.dispenseDrugToPatient(
				aDrug()
						.withId(ANY_DRUG_ID)
						.withName(ANY_DRUG_NAME)
						.withIngredients(
								aDrugIngredient()
										.withId(ANY_INGREDIENT_ID)
										.withName(ingredientName)
										.withExpirationDate(yesterday)
										.build()
						)
						.build(),
				UNUSED_PATIENT
		);
	}

	@Test
	@Parameters(method = "ingredientsProducingAllergies")
	public void cantDispenseDrugIfProducesAllergyToPatient(String drugName, String ingredientName, String patientName,
														   String reason) throws DispenseDrugException {
		currentDate = toDate("2018-04-01");
		Date nextMonth = toDate("2018-05-01");

		DrugIngredient anIngredient = aDrugIngredient()
				.withId(ANY_INGREDIENT_ID)
				.withName(ingredientName)
				.withExpirationDate(nextMonth)
				.build();

		cantDispenseBecause(reason);

		service.dispenseDrugToPatient(
				aDrug()
						.withId(ANY_DRUG_ID)
						.withName(drugName)
						.withIngredients(anIngredient)
						.build(),
				aPatient()
						.withId(ANY_PATIENT_ID)
						.withName(patientName)
						.withAllergies(allergyTo(anIngredient))
						.build()
		);
	}

	@Test
	public void canDispenseDrugToPatientIfDrugIsSafe() throws DispenseDrugException {
		currentDate = toDate("2018-04-01");
		Date nextMonth = toDate("2018-05-01");
		Date nextYear = toDate("2019-04-01");

		Drug safeDrug = new Drug(1L, "Safe Drug");
		DrugIngredient safeIngredient = new DrugIngredient(2L, "Safe Ingredient", nextYear);
		safeDrug.add(safeIngredient);

		Patient patient = new Patient(14L, "Clark Kent");

		DrugIngredient kriptonite = new DrugIngredient(1L, "Kriptonite", nextMonth);
		patient.add(allergyTo(kriptonite));

		service.dispenseDrugToPatient(safeDrug, patient);

		assertThat(
				"Should create a new order",
				orderCreated,
				is(true)
		);
	}

	@Test
	public void cantDispenseDrugIfOrderServiceFailsToCreateNewOrder() throws DispenseDrugException {
		currentDate = toDate("2018-04-01");
		Date nextMonth = toDate("2018-05-01");
		Date nextYear = toDate("2019-04-01");

		Drug safeDrug = new Drug(1L, "Safe Drug");
		DrugIngredient safeIngredient = new DrugIngredient(2L, "Safe Ingredient", nextYear);
		safeDrug.add(safeIngredient);

		Patient patient = new Patient(14L, "Clark Kent");

		DrugIngredient kriptonite = new DrugIngredient(1L, "Kriptonite", nextMonth);
		patient.add(allergyTo(kriptonite));

		orderException = new OrderException(ORDER_EXCEPTION_MESSAGE);
		cantDispenseBecause(ORDER_EXCEPTION_MESSAGE);

		service.dispenseDrugToPatient(safeDrug, patient);
	}

	@SuppressWarnings("unused")
	private String[][] ingredientsProducingAllergies() {
		return new String[][]{
				new String[]{"Antibiotic", "Penicillin", "Bob", "Could not dispense drug Antibiotic cause patient Bob has allergy to Penicillin"},
				new String[]{"Other Drug", "Other Ingredient", "Patient Name", "Could not dispense drug Other Drug cause patient Patient Name has allergy to Other Ingredient"},
		};
	}

	private Date toDate(String aDate) {
		return LocalDate.parse(aDate).toDate();
	}

	private void cantDispenseBecause(String reason) {
		expectedException.expect(DispenseDrugException.class);
		expectedException.expectMessage(is(reason));
	}

	private Allergy allergyTo(DrugIngredient ingredient) {
		return new Allergy(ingredient.id());
	}

	@SuppressWarnings("unused")
	private String[][] drugsWithoutIngredients() {
		return new String[][]{
				new String[]{"Lipitor", "There are not ingredients for drug: Lipitor"},
				new String[]{"Xanax", "There are not ingredients for drug: Xanax"},
				new String[]{"A Drug Name", "There are not ingredients for drug: A Drug Name"},
		};
	}

	@SuppressWarnings("unused")
	private String[][] expiredIngredients() {
		return new String[][]{
				new String[]{"Vitamin A", "Ingredient Vitamin A is expired."},
				new String[]{"Vitamin B", "Ingredient Vitamin B is expired."},
				new String[]{"Ingredient Name", "Ingredient Ingredient Name is expired."},
		};
	}

	private class TestableDispenseDrugService extends DispenseDrugService {

		@Override
		protected List<DrugIngredient> ingredientsOf(Drug drug) {
			return drug.ingredients();
		}

		@Override
		protected Date currentDate() {
			return currentDate;
		}

		@Override
		protected void createNewOrder(Drug drug, Patient patient) throws OrderException {
			if (orderException != null)
				throw orderException;

			orderCreated = true;
		}
	}
}