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

import java.util.Date;

public class DrugIngredient {
    private final Long _id;
    private final String _name;
    private final Date _expirationDate;

    public DrugIngredient(Long id, String name, Date expirationDate) {
        this._id = id;
        this._name = name;
        this._expirationDate = expirationDate;
    }

    public Long id() {
        return _id;
    }

    public String name() {
        return _name;
    }

    public Date expirationDate() {
        return _expirationDate;
    }

    public static final class DrugIngredientBuilder {
        private Long _id;
        private String _name;
        private Date _expirationDate;

        private DrugIngredientBuilder() {
        }

        public static DrugIngredientBuilder aDrugIngredient() {
            return new DrugIngredientBuilder();
        }

        public DrugIngredientBuilder withId(Long _id) {
            this._id = _id;
            return this;
        }

        public DrugIngredientBuilder withName(String _name) {
            this._name = _name;
            return this;
        }

        public DrugIngredientBuilder withExpirationDate(Date _expirationDate) {
            this._expirationDate = _expirationDate;
            return this;
        }

        public DrugIngredient build() {
            return new DrugIngredient(this._id, this._name, this._expirationDate);
        }
    }
}
