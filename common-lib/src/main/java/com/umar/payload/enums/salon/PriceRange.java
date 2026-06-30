package com.umar.payload.enums.salon;

public enum PriceRange {
    CHEAP("$"),
    MODERATE("$$"),
    EXPENSIVE("$$$"),
    LUXURY("$$$$");

    private final String label;

    PriceRange(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }}
