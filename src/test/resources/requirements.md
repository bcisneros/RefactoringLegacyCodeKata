# Tests we need

- Should not dispense a drug without ingredients: 
Throw a DispenseDrugException with message: "There are not ingredients for drug: %drugName%".

- Should not dispense a drug with expired ingredients:
Throw a DispenseDrugException with message: "Ingredient %ingredientName% is expired.".

- Should not dispense a drug if any ingredient produces an allergy to patient:
"Could not dispense drug %drugName% cause patient %patientName% has allergy to %ingredientNAme%" 

- Should be able to create new orders when no Validation Errors exist

- Should not dispense a drug if order creation fails