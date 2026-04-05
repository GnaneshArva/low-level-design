package ratelimiter.config;

/**
 * Immutable configuration for a rate limiter.
 * Centralizes all tunable parameters — avoids magic numbers across strategies.
 */
public final class RateLimitConfig {

    private final int limit;           // max requests allowed in the window
    private final long windowSizeMs;   // sliding/fixed window size in milliseconds
    private final int refillRate;      // tokens added per second (token bucket only)

    private RateLimitConfig(Builder builder) {
        this.limit = builder.limit;
        this.windowSizeMs = builder.windowSizeMs;
        this.refillRate = builder.refillRate;
    }

    public int getLimit() {
        return limit;
    }

    public long getWindowSizeMs() {
        return windowSizeMs;
    }

    public int getRefillRate() {
        return refillRate;
    }

    // Builder pattern for readable, validated construction
    public static final class Builder {

        private int limit;
        private long windowSizeMs;
        private int refillRate;

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder windowSizeMs(long windowSizeMs) {
            this.windowSizeMs = windowSizeMs;
            return this;
        }

        public Builder refillRate(int refillRate) {
            this.refillRate = refillRate;
            return this;
        }

        public RateLimitConfig build() {
            if (limit <= 0) throw new IllegalArgumentException("Limit must be positive");
            if (windowSizeMs <= 0) throw new IllegalArgumentException("Window size must be positive");
            if (refillRate < 0) throw new IllegalArgumentException("Refill rate must be non-negative");
            return new RateLimitConfig(this);
        }
    }

    @Override
    public String toString() {
        return "RateLimitConfig{limit=" + limit + ", windowSizeMs=" + windowSizeMs + ", refillRate=" + refillRate + "}";
    }
}
