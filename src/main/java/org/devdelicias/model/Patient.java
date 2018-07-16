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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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
import java.util.List;

import static java.util.Arrays.asList;

public class Patient {
    private final Long _id;
    private final String _name;
    private final List<Allergy> _allergies;

    public Patient(Long id, String name) {
        _id = id;
        _name = name;
        _allergies = new ArrayList<>();
    }

    public Long id() {
        return _id;
    }

    public String name() {
        return _name;
    }

    public List<Allergy> allergies() {
        return _allergies;
    }

    public void add(Allergy... allergies) {
        this._allergies.addAll(asList(allergies));
    }

    public void add(List<Allergy> allergies) {
        this._allergies.addAll(allergies);
    }

    public static final class PatientBuilder {
        private Long _id;
        private String _name;
        private List<Allergy> _allergies = new ArrayList<>();

        private PatientBuilder() {
        }

        public static PatientBuilder aPatient() {
            return new PatientBuilder();
        }

        public PatientBuilder withId(Long _id) {
            this._id = _id;
            return this;
        }

        public PatientBuilder withName(String _name) {
            this._name = _name;
            return this;
        }

        public PatientBuilder withAllergies(Allergy... _allergies) {
            this._allergies = new ArrayList<>(Arrays.asList(_allergies));
            return this;
        }

        public Patient build() {
            Patient patient = new Patient(this._id, this._name);
            patient.add(this._allergies);
            return patient;
        }
    }
}
