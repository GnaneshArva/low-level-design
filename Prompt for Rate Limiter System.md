Act as a Principal Java Engineer designing a Low Level Design (LLD)
solution for a Rate Limiter System.

The solution must be interview-grade, production-inspired, and
demonstrate strong object-oriented design, SOLID principles, and design
patterns.

======================== OBJECTIVE ======================== Design and
implement a Rate Limiter System in Java that supports: - Multiple rate
limiting algorithms - Per-user rate limiting - Global rate limiting -
Request allowance/denial - Extensible policies - High-performance
decision making

======================== MANDATORY DESIGN CONSTRAINTS
======================== 1. Apply OOP principles explicitly: -
Encapsulation - Abstraction - Inheritance (only where appropriate) -
Polymorphism

2.  Follow ALL SOLID principles:
    -   SRP: Each class must have one clear responsibility
    -   OCP: New rate limiting algorithms must be added without
        modifying existing code
    -   LSP: Subtypes must be substitutable
    -   ISP: Prefer small, focused interfaces
    -   DIP: Depend on abstractions, not implementations
3.  Use Design Patterns where they naturally fit:
    -   Strategy Pattern → Rate limiting algorithm
    -   Factory Pattern → Strategy creation
    -   Decorator Pattern → Layered limits (user + global)
    -   Facade Pattern → Simplified rate limiter API
4.  Favor composition over inheritance
5.  Avoid God objects
6.  Avoid static utility-heavy design

======================== FUNCTIONAL REQUIREMENTS
======================== 1. Allow or reject incoming requests 2. Support
multiple rate limiting algorithms 3. Support per-user rate limiting 4.
Support global rate limiting 5. Track request timestamps 6. Reset limits
based on time window 7. Provide remaining quota 8. Allow algorithm
switching

======================== DOMAIN MODEL (EXPECTED)
========================

-   RateLimiter

    -   entry point API
    -   allowRequest(userId)

-   RateLimitStrategy (interface)

    -   allowRequest()
    -   getRemainingTokens()

-   FixedWindowStrategy

-   SlidingWindowStrategy

-   TokenBucketStrategy

-   RateLimitConfig

    -   limit
    -   windowSize
    -   refillRate

-   RateLimiterFactory

    -   creates strategy instances

-   UserRateLimiter

    -   per-user limiter

-   GlobalRateLimiter

    -   system-wide limiter

======================== BUSINESS RULES ======================== -
Requests exceeding limit must be rejected - Limits reset after time
window - Each user has independent limit - Global limit applies across
users - Fast decision required for each request - No blocking operations

======================== CODING STANDARDS (STRICT)
======================== 1. Follow Java naming conventions 2. Classes
must be cohesive and small 3. No public fields 4. Use final where
applicable 5. Use immutable objects where possible 6. Proper access
modifiers 7. No Lombok, no frameworks, no external libraries 8. Avoid
magic numbers 9. Write clean, readable, interview-friendly code

======================== ERROR HANDLING ======================== - Throw
meaningful custom exceptions - Validate inputs at boundaries - Do NOT
silently ignore errors - Handle invalid configuration

======================== EXTENSIBILITY EXPECTATIONS
======================== - Adding new rate limiting algorithms must not
modify existing classes - Support distributed rate limiting later -
Allow pluggable storage (in-memory, Redis) - Multiple layered rate
limits should be supported

======================== DELIVERABLES ======================== 1.
Complete Java implementation with package structure 2. Clear separation
of concerns 3. Minimal but meaningful comments explaining design
decisions 4. A Main class demonstrating: - Creating rate limiter -
Sending requests - Allow/deny output - Switching strategies

======================== INTERVIEW EXPECTATION
======================== - Code should be explainable in 20 minutes -
Trade-offs must be obvious - Design must scale from simple to complex

======================== IMPORTANT RULES ======================== - DO
NOT over-engineer - DO NOT add unnecessary patterns - DO NOT include
unit tests unless asked - Output ONLY Java source code

Note: ZIP all the files to a folder.