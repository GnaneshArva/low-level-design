package ratelimiter.factory;

/**
 * Enum of supported rate limiting algorithms.
 * Decouples callers from string-based algorithm selection — type-safe switching.
 */
public enum AlgorithmType {
    FIXED_WINDOW,
    SLIDING_WINDOW,
    TOKEN_BUCKET
}
