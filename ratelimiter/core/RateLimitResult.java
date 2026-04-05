package ratelimiter.core;

/**
 * Immutable result of a rate limit check.
 * Avoids throwing exceptions for normal rejection flow (high-frequency path).
 * Callers inspect .isAllowed() and use metadata for response headers or logging.
 */
public final class RateLimitResult {

    private final boolean allowed;
    private final int remainingTokens;
    private final String reason;

    private RateLimitResult(boolean allowed, int remainingTokens, String reason) {
        this.allowed = allowed;
        this.remainingTokens = remainingTokens;
        this.reason = reason;
    }

    public static RateLimitResult allow(int remainingTokens) {
        return new RateLimitResult(true, remainingTokens, null);
    }

    public static RateLimitResult deny(String reason, int remainingTokens) {
        return new RateLimitResult(false, remainingTokens, reason);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public int getRemainingTokens() {
        return remainingTokens;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return allowed
                ? "ALLOWED [remaining=" + remainingTokens + "]"
                : "DENIED  [reason=" + reason + ", remaining=" + remainingTokens + "]";
    }
}
