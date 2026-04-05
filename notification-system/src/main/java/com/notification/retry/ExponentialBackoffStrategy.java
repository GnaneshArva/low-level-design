package com.notification.retry;

/**
 * Exponential backoff: delay doubles on each attempt.
 * e.g., 500ms → 1000ms → 2000ms → 4000ms
 *
 * Preferred for transient failures (network blips, rate limits).
 * Reduces thundering herd when many notifications fail simultaneously.
 */
public class ExponentialBackoffStrategy implements BackoffStrategy {

    private static final long MAX_DELAY_MS = 30_000L; // Cap at 30 seconds

    @Override
    public long computeDelay(long initialDelayMs, int attemptNumber) {
        long delay = initialDelayMs * (1L << (attemptNumber - 1)); // initialDelay * 2^(n-1)
        return Math.min(delay, MAX_DELAY_MS);
    }
}
