package com.lld.filestorage.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composite Pattern — component base.
 * Both File and Folder are FileSystemNodes; callers can treat them uniformly.
 *
 * SRP  : Owns identity, ownership and permission data — nothing else.
 * LSP  : Concrete subtypes add behaviour without breaking the base contract.
 * DIP  : Depends on User/Permission abstractions, not concrete storage.
 */
public abstract class FileSystemNode {

    private final String  name;
    private final User    owner;
    private final Instant createdTime;

    // Permissions stored on the node; inherited by children at query time.
    private final List<Permission> permissions = new ArrayList<>();

    protected FileSystemNode(final String name, final User owner) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        if (owner == null)                  throw new IllegalArgumentException("owner required");
        this.name        = name;
        this.owner       = owner;
        this.createdTime = Instant.now();
    }

    // ── Identity ────────────────────────────────────────────────────────────

    public String  getName()        { return name; }
    public User    getOwner()       { return owner; }
    public Instant getCreatedTime() { return createdTime; }

    // ── Permission management ────────────────────────────────────────────────

    public void addPermission(final Permission permission) {
        if (permission == null) throw new IllegalArgumentException("permission required");
        permissions.add(permission);
    }

    public List<Permission> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }

    /**
     * Checks whether a user has at minimum the requested access level.
     * Owner always has full access; WRITE implies READ.
     *
     * Trade-off: permission check lives here (not a separate Checker class) to
     * keep the domain model self-contained for interview clarity. In production
     * this would be an ACL service with caching and policy evaluation.
     */
    public boolean hasAccess(final User user, final AccessType required) {
        if (owner.equals(user)) return true;
        for (Permission p : permissions) {
            if (p.getUser().equals(user)) {
                if (p.getAccessType() == required)              return true;
                if (p.getAccessType() == AccessType.WRITE)      return true; // WRITE ⊇ READ
            }
        }
        return false;
    }

    // ── Composite contract ───────────────────────────────────────────────────

    /** Composite pattern leaf/composite discriminator — overridden in Folder. */
    public abstract boolean isFolder();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{name='" + name + "', owner=" + owner.getUserId() + "}";
    }
}
