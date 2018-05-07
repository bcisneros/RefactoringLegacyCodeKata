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
 * Class Drug.
 *
 * @since 1.0
 */
public class Drug {
    /**
     * The initial capacity of list of ingredients.
     */
    private static final int INITIAL_CAPACITY = 10;
    /**
     * The Drug identifier.
     */
    private final Long id;
    /**
     * The Drug Name.
     */
    private final String name;

    /**
     * The collection of ingredients of the Drug.
     */
    private final List<DrugIngredient> ingredients;

    /**
     * Creates a new Drug with a given identifier and name.
     * By default a Drug has not any ingredient.
     *
     * @param id The Drug identifier
     * @param name The Drug name
     */
    public Drug(final Long id, final String name) {
        this.id = id;
        this.name = name;
        this.ingredients = new ArrayList<>(Drug.INITIAL_CAPACITY);
    }

    /**
     * Returns the Drug Identifier.
     *
     * @return A Long number.
     */
    public final Long identifier() {
        return this.id;
    }

    /**
     * Returns the Drug name.
     *
     * @return The name of the Drug.
     */
    public final String fullName() {
        return this.name;
    }

    /**
     * Allows to add more ingredients to the Drug.
     *
     * @param more One or more ingredients to add
     */
    public final void add(final DrugIngredient... more) {
        this.ingredients.addAll(Arrays.asList(more));
    }

    /**
     * Gets a reference of the ingredient list, but is not modifiable.
     *
     * @return An unmodifiable list of ingredients
     */
    public final List<DrugIngredient> allIngredients() {
        return Collections.unmodifiableList(this.ingredients);
    }
}
