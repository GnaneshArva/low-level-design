package ratelimiter.core;

import ratelimiter.algorithm.RateLimitStrategy;

/**
 * Per-user rate limiter.
 * Owns a single strategy instance and forwards request evaluation to it.
 *
 * SRP: This class is responsible only for per-user limiting.
 * It does not own algorithm logic — that belongs to the strategy.
 *
 * Strategy is mutable (switchable at runtime) — enables live policy changes
 * without restarting the service.
 */
public final class UserRateLimiter {

    private final String userId;
    private volatile RateLimitStrategy strategy; // volatile for safe strategy swap

    public UserRateLimiter(String userId, RateLimitStrategy strategy) {
        if (userId == null || userId.isBlank()) throw new IllegalArgumentException("userId must not be blank");
        if (strategy == null) throw new IllegalArgumentException("Strategy must not be null");
        this.userId = userId;
        this.strategy = strategy;
    }

    public boolean allowRequest(RequestContext context) {
        return strategy.allowRequest(context);
    }

    public int getRemainingTokens() {
        return strategy.getRemainingTokens(userId);
    }

    /**
     * Hot-swap the rate limiting algorithm at runtime.
     * New requests immediately use the new strategy.
     */
    public void switchStrategy(RateLimitStrategy newStrategy) {
        if (newStrategy == null) throw new IllegalArgumentException("New strategy must not be null");
        this.strategy = newStrategy;
    }

    public String getUserId() {
        return userId;
    }

    public String currentAlgorithm() {
        return strategy.algorithmName();
    }
}
