package com.notification.model;

/**
 * Lifecycle states for a notification delivery attempt.
 * SRP: Status tracking is isolated here, not embedded in channel logic.
 */
public enum DeliveryStatus {
    PENDING,
    IN_PROGRESS,
    DELIVERED,
    FAILED,
    FALLBACK_DELIVERED,
    EXHAUSTED       // All retries + fallback failed
}
