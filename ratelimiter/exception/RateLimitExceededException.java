package ratelimiter.exception;

/**
 * Thrown when a caller's request is rejected due to rate limiting.
 * Carries context (userId, remaining quota) for observability and caller feedback.
 */
public class RateLimitExceededException extends RuntimeException {

    private final String userId;
    private final int remaining;

    public RateLimitExceededException(String userId, int remaining) {
        super("Rate limit exceeded for user: " + userId + " | Remaining: " + remaining);
        this.userId = userId;
        this.remaining = remaining;
    }

    public String getUserId() {
        return userId;
    }

    public int getRemaining() {
        return remaining;
    }
}
