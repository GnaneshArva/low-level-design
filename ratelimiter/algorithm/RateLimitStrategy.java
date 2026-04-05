package ratelimiter.algorithm;

import ratelimiter.core.RequestContext;

/**
 * Strategy Pattern — core abstraction for rate limiting algorithms.
 *
 * OCP: New algorithms (LeakyBucket, ConcurrencyLimit, etc.) are added
 * by implementing this interface, without touching existing code.
 *
 * ISP: Interface is intentionally narrow — only what every strategy must do.
 * No default methods that impose behavior on implementors.
 */
public interface RateLimitStrategy {

    /**
     * Evaluate whether the given request should be allowed.
     *
     * @param context request metadata
     * @return true if allowed, false if rejected
     */
    boolean allowRequest(RequestContext context);

    /**
     * Remaining quota for the caller at this moment.
     * Used for response headers (X-RateLimit-Remaining) and dashboards.
     */
    int getRemainingTokens(String userId);

    /**
     * Human-readable algorithm name — used in logs and diagnostics.
     */
    String algorithmName();
}
