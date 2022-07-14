package com.filesharing.springjwt.models;

public enum ELanguage {
    REACT(0),
    ANGULAR(1),
    JAVA(2),
    JAVASCRIPT(3),
    PYTHON(4),
    CSHARP(5),
    CPLUSPLUS(6),
    C(7),
    SQL(8),
    HTML_CSS(9),
    PHP(10),
    OTHER_AUTRE(10);

    private final int value;
    private ELanguage(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
