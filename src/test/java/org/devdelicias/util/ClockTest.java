package org.devdelicias.util;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class ClockTest {

    @Test
    public void provide_current_date() {
        assertThat(
                new Clock().currentDateTime(),
                equalTo(new Date())
        );

    }
}