package ratelimiter.core;

/**
 * Immutable value object representing an incoming request.
 * Decouples strategy logic from raw primitive parameters — extensible
 * without changing strategy interfaces (e.g. add IP, region later).
 */
public final class RequestContext {

    private final String userId;
    private final long timestampMs;

    public RequestContext(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId must not be null or blank");
        }
        this.userId = userId;
        this.timestampMs = System.currentTimeMillis();
    }

    public String getUserId() {
        return userId;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    @Override
    public String toString() {
        return "RequestContext{userId='" + userId + "', timestampMs=" + timestampMs + "}";
    }
}
