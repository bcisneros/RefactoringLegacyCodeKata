package org.devdelicias.model;

public class Drug {
    private final Long _id;
    private final String _name;

    public Drug(Long id, String name) {
        _id = id;
        _name = name;
    }

    public Long id() {
        return _id;
    }

    public String name() {
        return _name;
    }
}
