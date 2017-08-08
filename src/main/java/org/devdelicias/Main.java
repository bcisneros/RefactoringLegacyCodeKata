package org.devdelicias;

import org.devdelicias.model.Drug;
import org.devdelicias.model.Patient;
import org.devdelicias.service.DispenseDrugException;
import org.devdelicias.service.DispenseDrugService;
import org.devdelicias.service.DrugException;

public class Main {

    public static void main(String[] args) throws DrugException, DispenseDrugException {
        new DispenseDrugService().dispenseDrugToPatient(new Drug(), new Patient());
    }
}
