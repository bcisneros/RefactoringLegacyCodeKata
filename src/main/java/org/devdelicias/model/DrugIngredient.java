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

    public boolean isExpiredAt(Date givenDate) {

        return expirationDate().before(givenDate);
    }
}
