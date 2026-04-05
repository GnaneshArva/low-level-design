package com.notification.model;

/**
 * Encapsulates all contact addresses for a recipient.
 * Immutable value object - no setters, all fields final.
 *
 * Design note: A single Recipient carries addresses for all possible channels,
 * so the system can fall back to another channel without needing a new object.
 */
public final class Recipient {

    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String deviceToken;   // For push notifications

    private Recipient(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.deviceToken = builder.deviceToken;
    }

    public String getName()        { return name; }
    public String getEmail()       { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getDeviceToken() { return deviceToken; }

    @Override
    public String toString() {
        return "Recipient{name='" + name + "'}";
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static final class Builder {
        private final String name;
        private String email;
        private String phoneNumber;
        private String deviceToken;

        private Builder(String name) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Recipient name must not be blank");
            }
            this.name = name;
        }

        public Builder email(String email)             { this.email = email; return this; }
        public Builder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public Builder deviceToken(String deviceToken) { this.deviceToken = deviceToken; return this; }

        public Recipient build() { return new Recipient(this); }
    }
}
