package ratelimiter.core;

import ratelimiter.algorithm.RateLimitStrategy;

/**
 * Global (system-wide) rate limiter.
 * Guards the entire system regardless of which user is making requests.
 *
 * Uses a shared userId key ("__global__") so that all requests
 * are counted against the same bucket in the underlying strategy.
 *
 * SRP: Focused solely on system-level limiting.
 */
public final class GlobalRateLimiter {

    // Sentinel key used across all strategy implementations to track global state
    private static final String GLOBAL_USER_KEY = "__global__";

    private final RateLimitStrategy strategy;

    public GlobalRateLimiter(RateLimitStrategy strategy) {
        if (strategy == null) throw new IllegalArgumentException("Strategy must not be null");
        this.strategy = strategy;
    }

    public boolean allowRequest() {
        RequestContext context = new RequestContext(GLOBAL_USER_KEY);
        return strategy.allowRequest(context);
    }

    public int getRemainingTokens() {
        return strategy.getRemainingTokens(GLOBAL_USER_KEY);
    }

    public String currentAlgorithm() {
        return strategy.algorithmName();
    }
}
