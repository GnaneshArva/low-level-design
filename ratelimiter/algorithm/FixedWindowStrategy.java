package ratelimiter.algorithm;

import ratelimiter.config.RateLimitConfig;
import ratelimiter.core.RequestContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fixed Window Algorithm:
 * - Divides time into fixed windows (e.g. 0-60s, 60-120s).
 * - Each user gets a fresh counter at the start of each window.
 *
 * Trade-off: Simple and fast (O(1)), but allows burst at window boundaries
 * (2x the limit can pass if burst hits the tail and head of two windows).
 *
 * Thread Safety: ConcurrentHashMap + AtomicInteger ensure correctness
 * without coarse-grained locking.
 */
public final class FixedWindowStrategy implements RateLimitStrategy {

    private final RateLimitConfig config;

    // Per-user state: windowStart timestamp + request count in current window
    private final Map<String, long[]> windowState = new ConcurrentHashMap<>();
    // Index constants for the state array (avoids magic numbers)
    private static final int WINDOW_START = 0;
    private static final int COUNT = 1;

    public FixedWindowStrategy(RateLimitConfig config) {
        if (config == null) throw new IllegalArgumentException("Config must not be null");
        this.config = config;
    }

    @Override
    public synchronized boolean allowRequest(RequestContext context) {
        final String userId = context.getUserId();
        final long now = context.getTimestampMs();

        long[] state = windowState.computeIfAbsent(userId, k -> new long[]{now, 0});

        // If current time exceeds the window, reset the counter
        if (now - state[WINDOW_START] >= config.getWindowSizeMs()) {
            state[WINDOW_START] = now;
            state[COUNT] = 0;
        }

        if (state[COUNT] < config.getLimit()) {
            state[COUNT]++;
            return true;
        }

        return false;
    }

    @Override
    public int getRemainingTokens(String userId) {
        long[] state = windowState.get(userId);
        if (state == null) return config.getLimit();
        long now = System.currentTimeMillis();
        if (now - state[WINDOW_START] >= config.getWindowSizeMs()) return config.getLimit();
        return Math.max(0, config.getLimit() - (int) state[COUNT]);
    }

    @Override
    public String algorithmName() {
        return "FixedWindow";
    }
}
