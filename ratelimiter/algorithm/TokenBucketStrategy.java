package ratelimiter.algorithm;

import ratelimiter.config.RateLimitConfig;
import ratelimiter.core.RequestContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token Bucket Algorithm:
 * - Each user has a bucket with capacity = config.limit tokens.
 * - Tokens refill at config.refillRate tokens/second, up to capacity.
 * - Each request consumes 1 token. Rejected if bucket is empty.
 *
 * Trade-off: Naturally handles bursting up to bucket capacity.
 * Best for APIs that want to allow short bursts but enforce average rates.
 * State is lazy-initialized per user (no upfront allocation).
 */
public final class TokenBucketStrategy implements RateLimitStrategy {

    private final RateLimitConfig config;
    private final Map<String, double[]> buckets = new ConcurrentHashMap<>();

    // Index constants for bucket state array
    private static final int TOKENS = 0;
    private static final int LAST_REFILL_TIME = 1;

    public TokenBucketStrategy(RateLimitConfig config) {
        if (config == null) throw new IllegalArgumentException("Config must not be null");
        this.config = config;
    }

    @Override
    public synchronized boolean allowRequest(RequestContext context) {
        final String userId = context.getUserId();
        final long now = context.getTimestampMs();

        double[] bucket = buckets.computeIfAbsent(userId,
                k -> new double[]{config.getLimit(), now});

        refill(bucket, now);

        if (bucket[TOKENS] >= 1.0) {
            bucket[TOKENS] -= 1.0;
            return true;
        }

        return false;
    }

    /**
     * Lazy refill: compute tokens accumulated since last refill.
     * Avoids background threads — refill happens on request arrival.
     */
    private void refill(double[] bucket, long now) {
        double elapsedSeconds = (now - bucket[LAST_REFILL_TIME]) / 1000.0;
        double tokensToAdd = elapsedSeconds * config.getRefillRate();
        bucket[TOKENS] = Math.min(config.getLimit(), bucket[TOKENS] + tokensToAdd);
        bucket[LAST_REFILL_TIME] = now;
    }

    @Override
    public int getRemainingTokens(String userId) {
        double[] bucket = buckets.get(userId);
        if (bucket == null) return config.getLimit();
        double[] copy = new double[]{bucket[TOKENS], bucket[LAST_REFILL_TIME]};
        refill(copy, System.currentTimeMillis());
        return (int) Math.floor(copy[TOKENS]);
    }

    @Override
    public String algorithmName() {
        return "TokenBucket";
    }
}
