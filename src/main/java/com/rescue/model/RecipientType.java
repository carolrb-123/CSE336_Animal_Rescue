package com.rescue.model;

/**
 * Who a message is for. The Notifier picks a default channel for each type
 * (Strategy pattern: the type decides the strategy at runtime).
 */
public enum RecipientType {
    DONOR("Donor"),
    VOLUNTEER("Volunteer"),
    ADOPTER("Adopter"),
    COORDINATOR("Coordinator"),
    PUBLIC("Public (social page)");

    private final String label;

    RecipientType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
