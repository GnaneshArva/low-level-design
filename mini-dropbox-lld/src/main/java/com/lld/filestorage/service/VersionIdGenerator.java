package com.lld.filestorage.service;

import java.util.UUID;

/**
 * Generates unique, opaque version identifiers.
 *
 * SRP: Only ID generation — no other responsibility.
 * OCP: Swap UUID for Snowflake, ULID, or timestamp-based IDs without touching callers.
 * DIP: Commands depend on this interface rather than UUID directly.
 *
 * Trade-off: UUID v4 is random and globally unique but not sortable.
 * In production, ULID (lexicographically sortable) is preferred for versioning.
 */
public final class VersionIdGenerator {

    public String generate() {
        return "v_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
