package org.devdelicias.service;

import org.devdelicias.model.Drug;
import org.devdelicias.model.Patient;
import org.junit.Test;

public class OrderServiceTest {

    private static final Drug UNUSED_DRUG = null;
    private static final Patient UNUSED_PATIENT = null;

    @Test(expected = UnsupportedOperationException.class)
    public void create_new_order() throws OrderException {
        new OrderService().createNewOrder(UNUSED_DRUG, UNUSED_PATIENT);
    }
}