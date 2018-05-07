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

import java.util.Date;

/**
 * Class Ingredient.
 *
 * @since 1.0
 */
public class Ingredient {
    /**
     * The identifier.
     */
    private final Long id;
    /**
     * The name.
     */
    private final String name;
    /**
     * The expiration date.
     */
    private final Date expires;

    /**
     * Creates new Ingredient.
     *
     * @param id The identifier.
     * @param name Then name.
     * @param expires The expiration date.
     */
    public Ingredient(final Long id, final String name, final Date expires) {
        this.id = id;
        this.name = name;
        this.expires = expires;
    }

    /**
     * Returns the identifier.
     *
     * @return The identifier.
     */
    public final Long identifier() {
        return this.id;
    }

    /**
     * Returns the name.
     *
     * @return The name
     */
    public final String fullName() {
        return this.name;
    }

    /**
     * Return the expiration date.
     *
     * @return The expiration date.
     */
    public final Date expirationDate() {
        return this.expires;
    }
}
