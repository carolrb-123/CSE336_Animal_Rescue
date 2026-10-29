package com.rescue.model;

/**
 * A simple Recipient for people who are not stored elsewhere: someone typed into the
 * Communication screen, the coordinator, or the public social page.
 * Donors, volunteers and adopters can be passed to the Notifier directly, since they implement Recipient.
 */
public class Contact implements Recipient {

    /** The rescue's public social page. The only recipient a social post may go to. */
    public static final Contact PUBLIC_AUDIENCE = new Contact("Public followers", null, null);

    private final String name;
    private final String email;
    private final String phone;

    public Contact(String name, String email, String phone) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Please enter the recipient's name.");
        }
        this.name = name.trim();
        this.email = blankToNull(email);
        this.phone = blankToNull(phone);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    private static String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
