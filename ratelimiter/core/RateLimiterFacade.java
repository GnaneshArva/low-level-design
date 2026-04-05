package ratelimiter.core;

import ratelimiter.algorithm.RateLimitStrategy;
import ratelimiter.decorator.LayeredRateLimiter;
import ratelimiter.factory.AlgorithmType;
import ratelimiter.factory.RateLimitStrategyFactory;
import ratelimiter.config.RateLimitConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Facade Pattern — single entry point for all rate limiting decisions.
 *
 * Hides the complexity of: per-user limiter creation, global limiter,
 * strategy factory, and decorator composition.
 *
 * Callers only know about allowRequest(userId) — they are shielded
 * from algorithm selection, layering logic, and state management.
 *
 * DIP: Internally wires abstractions — callers never touch concrete classes.
 */
public final class RateLimiterFacade {

    private final RateLimitConfig userConfig;
    private final GlobalRateLimiter globalLimiter;
    private AlgorithmType algorithmType;

    // Lazy per-user limiter registry
    private final Map<String, UserRateLimiter> userLimiters = new ConcurrentHashMap<>();

    public RateLimiterFacade(AlgorithmType algorithmType,
                              RateLimitConfig userConfig,
                              RateLimitConfig globalConfig) {
        if (algorithmType == null || userConfig == null || globalConfig == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        }
        this.algorithmType = algorithmType;
        this.userConfig = userConfig;

        RateLimitStrategy globalStrategy = RateLimitStrategyFactory.create(algorithmType, globalConfig);
        this.globalLimiter = new GlobalRateLimiter(globalStrategy);
    }

    /**
     * Primary API: allow or deny a request for the given user.
     * Thread-safe: ConcurrentHashMap ensures only one limiter per userId.
     */
    public RateLimitResult allowRequest(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId must not be blank");
        }

        UserRateLimiter userLimiter = userLimiters.computeIfAbsent(userId, this::createUserLimiter);
        LayeredRateLimiter layered = new LayeredRateLimiter(userLimiter, globalLimiter);
        RequestContext context = new RequestContext(userId);
        return layered.evaluate(context);
    }

    /**
     * Live algorithm switch — affects all future requests for all users.
     * Existing per-user limiters are replaced on next request.
     * Trade-off: Old limiter state is discarded on switch (acceptable in practice).
     */
    public void switchAlgorithm(AlgorithmType newType) {
        if (newType == null) throw new IllegalArgumentException("AlgorithmType must not be null");
        this.algorithmType = newType;

        // Invalidate all existing per-user limiters — new ones will be created lazily
        userLimiters.forEach((userId, limiter) -> {
            RateLimitStrategy newStrategy = RateLimitStrategyFactory.create(newType, userConfig);
            limiter.switchStrategy(newStrategy);
        });

        System.out.println("[RateLimiter] Switched algorithm to: " + newType);
    }

    public int getRemainingTokens(String userId) {
        UserRateLimiter limiter = userLimiters.get(userId);
        return limiter == null ? userConfig.getLimit() : limiter.getRemainingTokens();
    }

    private UserRateLimiter createUserLimiter(String userId) {
        RateLimitStrategy strategy = RateLimitStrategyFactory.create(algorithmType, userConfig);
        return new UserRateLimiter(userId, strategy);
    }
}
