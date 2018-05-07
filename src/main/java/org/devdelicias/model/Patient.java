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
package org.devdelicias.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class Patient.
 *
 * @since 1.0
 */
public class Patient {
    /**
     * The id.
     */
    private final Long id;
    /**
     * The name.
     */
    private final String name;
    /**
     * The allergies.
     */
    private final List<Allergy> allergies;

    /**
     * Creates a new Patient.
     *
     * @param id The Patient id.
     * @param name The Patient full name.
     */
    public Patient(final Long id, final String name) {
        this.id = id;
        this.name = name;
        this.allergies = new ArrayList<>(1);
    }

    /**
     * Gets the Patient Identifier.
     *
     * @return Patient Id.
     */
    public final Long identifier() {
        return this.id;
    }

    /**
     * Gets the Patient Full Name.
     *
     * @return Patient Full Name.
     */
    public final String fullName() {
        return this.name;
    }

    /**
     * Retrieves all Allergies but cannot be modified.
     *
     * @return All allergies of the patient.
     */
    public final List<Allergy> allAllergies() {
        return Collections.unmodifiableList(this.allergies);
    }

    /**
     * Allow to add new allergies to Patient.
     *
     * @param more New allergies.
     */
    public final void add(final Allergy... more) {
        this.allergies.addAll(Arrays.asList(more));
    }
}
