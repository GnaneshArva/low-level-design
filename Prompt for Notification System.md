Act as a Principal Java Engineer designing a Low Level Design (LLD)
solution for a Notification System.

The solution must be interview-grade, production-inspired, and
demonstrate strong object-oriented design, SOLID principles, and design
patterns.

======================== OBJECTIVE ======================== Design and
implement a Notification System in Java that supports: - Multiple
notification channels (Email, SMS, Push) - Retry mechanisms -
Prioritization - Templates - Failure handling - Extensible delivery
strategies

======================== MANDATORY DESIGN CONSTRAINTS
======================== 1. Apply OOP principles explicitly: -
Encapsulation - Abstraction - Inheritance (only where appropriate) -
Polymorphism

2.  Follow ALL SOLID principles:
    -   SRP: Each class must have one clear responsibility
    -   OCP: New notification channels must be added without modifying
        existing code
    -   LSP: Subtypes must be substitutable
    -   ISP: Prefer small, focused interfaces
    -   DIP: Depend on abstractions, not implementations
3.  Use Design Patterns where they naturally fit:
    -   Strategy Pattern → Notification channel delivery
    -   Factory Pattern → Notification channel creation
    -   Chain of Responsibility Pattern → Retry / failover handling
    -   Builder Pattern → Notification message construction
    -   Facade Pattern → Simplified notification API
4.  Favor composition over inheritance
5.  Avoid God objects
6.  Avoid static utility-heavy design

======================== FUNCTIONAL REQUIREMENTS
======================== 1. Send notification 2. Support multiple
channels (Email, SMS, Push) 3. Retry failed notifications 4. Prioritize
notifications 5. Use templates for message formatting 6. Support
fallback channel 7. Track delivery status 8. Handle failures gracefully

======================== DOMAIN MODEL (EXPECTED)
========================

-   Notification

    -   id
    -   recipient
    -   message
    -   priority
    -   channelType

-   NotificationService

    -   send(Notification)

-   NotificationChannel (interface)

    -   send(Notification)

-   EmailChannel

-   SmsChannel

-   PushChannel

-   RetryHandler (interface)

    -   handle(Notification)

-   RetryPolicy

    -   maxRetries
    -   backoffStrategy

-   TemplateEngine

    -   applyTemplate()

-   NotificationBuilder

    -   builds notification object

-   NotificationFactory

    -   creates channel instances

======================== BUSINESS RULES ======================== -
Failed notifications must be retried - Fallback channel used after
retries fail - High priority notifications processed first - Template
must be applied before sending - Invalid channel should throw exception

======================== CODING STANDARDS (STRICT)
======================== 1. Follow Java naming conventions 2. Classes
must be cohesive and small 3. No public fields 4. Use final where
applicable 5. Use immutable objects where possible 6. Proper access
modifiers 7. No Lombok, no frameworks, no external libraries 8. Avoid
magic numbers 9. Write clean, readable, interview-friendly code

======================== ERROR HANDLING ======================== - Throw
meaningful custom exceptions - Validate inputs at boundaries - Do NOT
silently ignore errors - Handle channel delivery failures

======================== EXTENSIBILITY EXPECTATIONS
======================== - Adding new notification channels must not
modify existing classes - Retry strategy should be pluggable - Template
engine should be replaceable - Priority handling should be extendable

======================== DELIVERABLES ======================== 1.
Complete Java implementation with package structure 2. Clear separation
of concerns 3. Minimal but meaningful comments explaining design
decisions 4. A Main class demonstrating: - Creating notifications -
Sending via different channels - Retry handling - Fallback channel -
Priority handling

======================== INTERVIEW EXPECTATION
======================== - Code should be explainable in 20 minutes -
Trade-offs must be obvious - Design must scale from simple to complex

======================== IMPORTANT RULES ======================== - DO
NOT over-engineer - DO NOT add unnecessary patterns - DO NOT include
unit tests unless asked - Output ONLY Java source code
