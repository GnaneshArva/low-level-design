package com.lld.filestorage.domain;

/**
 * Immutable value object pairing a user with an access level.
 * ISP: Only exposes what a permission checker needs — user + accessType.
 */
public final class Permission {

    private final User user;
    private final AccessType accessType;

    public Permission(final User user, final AccessType accessType) {
        if (user == null)       throw new IllegalArgumentException("user must not be null");
        if (accessType == null) throw new IllegalArgumentException("accessType must not be null");
        this.user       = user;
        this.accessType = accessType;
    }

    public User getUser()           { return user; }
    public AccessType getAccessType() { return accessType; }

    @Override
    public String toString() {
        return "Permission{user=" + user.getUserId() + ", access=" + accessType + "}";
    }
}
