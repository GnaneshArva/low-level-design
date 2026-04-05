package com.lld.filestorage.domain;

/**
 * OCP: New access levels (e.g. ADMIN, EXECUTE) can be added here
 * without touching any existing permission-checking logic.
 */
public enum AccessType {
    READ,
    WRITE
}
