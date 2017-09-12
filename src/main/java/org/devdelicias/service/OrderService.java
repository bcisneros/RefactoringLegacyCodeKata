package org.devdelicias.service;

import org.devdelicias.model.Drug;
import org.devdelicias.model.Patient;

public class OrderService {
    public static void createOrder(Drug drug, Patient patient) throws OrderException {
        throw new UnsupportedOperationException();
    }

    public void createNewOrder(Drug drug, Patient patient) throws OrderException {
        createOrder(drug, patient);
    }
}
