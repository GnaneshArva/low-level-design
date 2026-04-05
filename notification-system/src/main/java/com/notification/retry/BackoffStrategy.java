package com.notification.retry;

/**
 * Strategy interface for computing wait time between retry attempts.
 * ISP: Single method, single concern.
 *
 * Pluggable into RetryPolicy — callers never see the implementation.
 */
public interface BackoffStrategy {

    /**
     * @param initialDelayMs configured base delay
     * @param attemptNumber  1-based retry count (1 = first retry)
     * @return milliseconds to wait before the next attempt
     */
    long computeDelay(long initialDelayMs, int attemptNumber);
}
