package com.notification.retry;

/**
 * Immutable configuration for retry behavior.
 * Separating policy from handler allows swapping strategies (exponential, linear)
 * without changing the retry orchestration logic.
 */
public final class RetryPolicy {

    public static final int DEFAULT_MAX_RETRIES   = 3;
    public static final long DEFAULT_BACKOFF_MS   = 500L;

    private final int maxRetries;
    private final long initialBackoffMs;
    private final BackoffStrategy backoffStrategy;

    private RetryPolicy(Builder builder) {
        this.maxRetries       = builder.maxRetries;
        this.initialBackoffMs = builder.initialBackoffMs;
        this.backoffStrategy  = builder.backoffStrategy;
    }

    public int getMaxRetries()           { return maxRetries; }
    public long getInitialBackoffMs()    { return initialBackoffMs; }
    public BackoffStrategy getBackoffStrategy() { return backoffStrategy; }

    /**
     * Calculates wait time for the nth attempt.
     */
    public long getWaitTime(int attemptNumber) {
        return backoffStrategy.computeDelay(initialBackoffMs, attemptNumber);
    }

    @Override
    public String toString() {
        return String.format("RetryPolicy{maxRetries=%d, backoff=%s, initialMs=%d}",
                maxRetries, backoffStrategy.getClass().getSimpleName(), initialBackoffMs);
    }

    // ── Factory methods ───────────────────────────────────────────────────────

    public static RetryPolicy defaultPolicy() {
        return new Builder().build();
    }

    public static RetryPolicy noRetry() {
        return new Builder().maxRetries(0).build();
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private int maxRetries         = DEFAULT_MAX_RETRIES;
        private long initialBackoffMs  = DEFAULT_BACKOFF_MS;
        private BackoffStrategy backoffStrategy = new ExponentialBackoffStrategy();

        public Builder maxRetries(int maxRetries) {
            if (maxRetries < 0) throw new IllegalArgumentException("maxRetries must be >= 0");
            this.maxRetries = maxRetries; return this;
        }
        public Builder initialBackoffMs(long ms) {
            if (ms < 0) throw new IllegalArgumentException("Backoff must be >= 0");
            this.initialBackoffMs = ms; return this;
        }
        public Builder backoffStrategy(BackoffStrategy strategy) {
            this.backoffStrategy = strategy; return this;
        }

        public RetryPolicy build() { return new RetryPolicy(this); }
    }
}
