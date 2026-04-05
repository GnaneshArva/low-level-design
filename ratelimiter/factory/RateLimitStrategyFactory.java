package ratelimiter.factory;

import ratelimiter.algorithm.FixedWindowStrategy;
import ratelimiter.algorithm.RateLimitStrategy;
import ratelimiter.algorithm.SlidingWindowStrategy;
import ratelimiter.algorithm.TokenBucketStrategy;
import ratelimiter.config.RateLimitConfig;
import ratelimiter.exception.InvalidConfigException;

/**
 * Factory Pattern — creates RateLimitStrategy instances by type.
 *
 * OCP: Adding a new algorithm requires only a new case here,
 * not changes to any existing strategy or caller.
 *
 * DIP: Callers depend on RateLimitStrategy (abstraction),
 * not on concrete classes. The factory absorbs construction details.
 */
public final class RateLimitStrategyFactory {

    // Stateless factory — no instance state needed
    private RateLimitStrategyFactory() {}

    public static RateLimitStrategy create(AlgorithmType type, RateLimitConfig config) {
        if (type == null) throw new IllegalArgumentException("AlgorithmType must not be null");
        if (config == null) throw new IllegalArgumentException("Config must not be null");

        switch (type) {
            case FIXED_WINDOW:
                return new FixedWindowStrategy(config);
            case SLIDING_WINDOW:
                return new SlidingWindowStrategy(config);
            case TOKEN_BUCKET:
                return new TokenBucketStrategy(config);
            default:
                throw new InvalidConfigException("Unsupported algorithm type: " + type);
        }
    }
}
