Act as a Principal Java Engineer designing a Low Level Design (LLD)
solution for an In-Memory Cache Library.

The solution must be interview-grade, production-inspired, and
demonstrate strong object-oriented design, SOLID principles, and design
patterns.

======================== OBJECTIVE ======================== Design and
implement an In-Memory Cache Library in Java that supports: - Key-value
storage - Eviction policies (LRU, LFU) - TTL (time-to-live) - Size-based
eviction - Thread-safe operations (optional but extensible) - Extensible
eviction strategies

======================== MANDATORY DESIGN CONSTRAINTS
======================== 1. Apply OOP principles explicitly: -
Encapsulation - Abstraction - Inheritance (only where appropriate) -
Polymorphism

2.  Follow ALL SOLID principles:
    -   SRP: Each class must have one clear responsibility
    -   OCP: New eviction strategies must be added without modifying
        existing code
    -   LSP: Subtypes must be substitutable
    -   ISP: Prefer small, focused interfaces
    -   DIP: Depend on abstractions, not implementations
3.  Use Design Patterns where they naturally fit:
    -   Strategy Pattern → Eviction policy
    -   Factory Pattern → Cache creation
    -   Decorator Pattern → TTL support
    -   Facade Pattern → Cache API
    -   Builder Pattern → Cache configuration
4.  Favor composition over inheritance
5.  Avoid God objects
6.  Avoid static utility-heavy design

======================== FUNCTIONAL REQUIREMENTS
======================== 1. Put key-value pair 2. Get value by key 3.
Remove key 4. Evict entries when capacity reached 5. Support TTL
expiration 6. Support multiple eviction policies 7. Clear cache 8. Get
cache size

======================== DOMAIN MODEL (EXPECTED)
========================

-   Cache\<K, V\>

    -   put(K, V)
    -   get(K)
    -   remove(K)

-   CacheEntry

    -   key
    -   value
    -   expiryTime
    -   metadata (frequency, accessTime)

-   EvictionPolicy (interface)

    -   onPut()
    -   onGet()
    -   evict()

-   LRUEvictionPolicy

-   LFUEvictionPolicy

-   CacheStorage

    -   Map\<K, CacheEntry\>

-   CacheConfig

    -   capacity
    -   ttl
    -   evictionPolicy

-   CacheBuilder

    -   builds cache configuration

-   CacheFactory

    -   creates cache instance

======================== BUSINESS RULES ======================== - Cache
size must not exceed capacity - Expired entries must not be returned -
Eviction triggered when capacity reached - Access updates usage
metadata - TTL optional

======================== CODING STANDARDS (STRICT)
======================== 1. Follow Java naming conventions 2. Classes
must be cohesive and small 3. No public fields 4. Use final where
applicable 5. Use immutable objects where possible 6. Proper access
modifiers 7. No Lombok, no frameworks, no external libraries 8. Avoid
magic numbers 9. Write clean, readable, interview-friendly code

======================== ERROR HANDLING ======================== - Throw
meaningful custom exceptions - Validate inputs at boundaries - Do NOT
silently ignore errors - Handle null keys appropriately

======================== EXTENSIBILITY EXPECTATIONS
======================== - Adding new eviction policies must not modify
existing classes - Support distributed cache later - Thread safety
should be pluggable - Storage backend should be replaceable

======================== DELIVERABLES ======================== 1.
Complete Java implementation with package structure 2. Clear separation
of concerns 3. Minimal but meaningful comments explaining design
decisions 4. A Main class demonstrating: - Creating cache - Put/get
operations - Eviction behavior - TTL expiration - Switching eviction
policy

======================== INTERVIEW EXPECTATION
======================== - Code should be explainable in 20 minutes -
Trade-offs must be obvious - Design must scale from simple to complex

======================== IMPORTANT RULES ======================== - DO
NOT over-engineer - DO NOT add unnecessary patterns - DO NOT include
unit tests unless asked - Output ONLY Java source code
