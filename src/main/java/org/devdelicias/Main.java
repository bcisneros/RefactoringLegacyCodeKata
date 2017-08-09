package org.devdelicias;

import org.devdelicias.model.Drug;
import org.devdelicias.model.Patient;
import org.devdelicias.service.DispenseDrugException;
import org.devdelicias.service.DispenseDrugService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws DispenseDrugException {
        LOGGER.info("Running DispenseDrugService");
        new DispenseDrugService().dispenseDrugToPatient(new Drug(), new Patient());
    }
}
