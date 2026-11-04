package com.rescue.model;

// Who the message is for. The Notifier uses this to choose email, SMS or a social post.
public enum RecipientType {
    DONOR,
    VOLUNTEER,
    ADOPTER,
    COORDINATOR,
    PUBLIC
}
