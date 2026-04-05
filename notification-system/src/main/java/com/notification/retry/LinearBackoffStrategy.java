package com.notification.retry;

/**
 * Linear backoff: constant delay between every attempt.
 * e.g., 500ms → 500ms → 500ms
 *
 * Simpler than exponential. Suitable when retry window is bounded
 * and you need predictable SLA (e.g., OTP delivery must complete in N seconds).
 */
public class LinearBackoffStrategy implements BackoffStrategy {

    @Override
    public long computeDelay(long initialDelayMs, int attemptNumber) {
        return initialDelayMs; // Same delay regardless of attempt number
    }
}
