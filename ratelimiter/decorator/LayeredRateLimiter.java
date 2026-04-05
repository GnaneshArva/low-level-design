package ratelimiter.decorator;

import ratelimiter.core.GlobalRateLimiter;
import ratelimiter.core.RateLimitResult;
import ratelimiter.core.RequestContext;
import ratelimiter.core.UserRateLimiter;

/**
 * Decorator Pattern — layers user-level and global limits together.
 *
 * Design: Both limiters are evaluated in sequence.
 * User limit is checked first (cheaper — fails fast for abusers).
 * Global limit is checked second (system-wide guard).
 *
 * This class wraps existing limiters without modifying them — OCP in action.
 * Supports future extension: add IP-level, tenant-level, etc. as more decorators.
 */
public final class LayeredRateLimiter {

    private final UserRateLimiter userLimiter;
    private final GlobalRateLimiter globalLimiter;

    public LayeredRateLimiter(UserRateLimiter userLimiter, GlobalRateLimiter globalLimiter) {
        if (userLimiter == null) throw new IllegalArgumentException("UserRateLimiter must not be null");
        if (globalLimiter == null) throw new IllegalArgumentException("GlobalRateLimiter must not be null");
        this.userLimiter = userLimiter;
        this.globalLimiter = globalLimiter;
    }

    /**
     * Evaluates both user and global limits.
     * A request must pass BOTH to be allowed.
     */
    public RateLimitResult evaluate(RequestContext context) {
        // Layer 1: per-user check
        if (!userLimiter.allowRequest(context)) {
            return RateLimitResult.deny(
                    "User quota exceeded for: " + context.getUserId(),
                    userLimiter.getRemainingTokens()
            );
        }

        // Layer 2: global system check
        if (!globalLimiter.allowRequest()) {
            return RateLimitResult.deny(
                    "Global system limit reached",
                    globalLimiter.getRemainingTokens()
            );
        }

        // Both layers passed
        return RateLimitResult.allow(userLimiter.getRemainingTokens());
    }
}
