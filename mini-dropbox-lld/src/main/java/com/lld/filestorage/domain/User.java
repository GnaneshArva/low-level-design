package com.lld.filestorage.domain;

/**
 * Immutable value object representing a system user.
 * SRP: Only holds identity data — no behaviour beyond equality.
 */
public final class User {

    private final String userId;
    private final String name;

    public User(final String userId, final String name) {
        if (userId == null || userId.isBlank()) throw new IllegalArgumentException("userId must not be blank");
        if (name == null || name.isBlank())     throw new IllegalArgumentException("name must not be blank");
        this.userId = userId;
        this.name   = name;
    }

    public String getUserId() { return userId; }
    public String getName()   { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return userId.equals(((User) o).userId);
    }

    @Override public int hashCode() { return userId.hashCode(); }
    @Override public String toString() { return "User{" + userId + ", " + name + "}"; }
}
