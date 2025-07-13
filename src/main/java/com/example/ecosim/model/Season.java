package com.example.ecosim.model;

public enum Season {
    SPRING, SUMMER, AUTUMN, WINTER;

    public Season next() {
        return values()[(ordinal() + 1) % values().length];
    }
}