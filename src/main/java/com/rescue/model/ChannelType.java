package com.rescue.model;

/** The ways the rescue can contact someone. There is one ChannelStrategy per channel. */
public enum ChannelType {
    EMAIL("Email"),
    SMS("SMS"),
    SOCIAL_POST("Social post");

    private final String label;

    ChannelType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
