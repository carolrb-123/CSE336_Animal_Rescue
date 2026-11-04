package com.rescue.model;

// A simple person we can send a message to (typed in the Communication screen,
// the coordinator, or the public social page).
public class Contact implements Recipient {

    // The rescue's public social page. A social post can only go to this contact.
    public static final Contact PUBLIC_AUDIENCE = new Contact("Public followers", null, null);

    private final String name;
    private final String email;
    private final String phone;

    public Contact(String name, String email, String phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter the recipient's name.");
        }
        this.name = name.trim();
        this.email = emptyToNull(email);
        this.phone = emptyToNull(phone);
    }

    // An empty text box means "no email" / "no phone", so we store null.
    private String emptyToNull(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        return text.trim();
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
}
