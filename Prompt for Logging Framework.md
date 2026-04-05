Act as a Principal Java Engineer designing a Low Level Design (LLD)
solution for a Logging Framework.

The solution must be interview-grade, production-inspired, and
demonstrate strong object-oriented design, SOLID principles, and design
patterns.

======================== OBJECTIVE ======================== Design and
implement a Logging Framework in Java that supports: - Multiple log
levels - Multiple appenders (Console, File, etc.) - Log formatting -
Asynchronous logging - Log rotation - Extensible logging configuration

======================== MANDATORY DESIGN CONSTRAINTS
======================== 1. Apply OOP principles explicitly: -
Encapsulation - Abstraction - Inheritance (only where appropriate) -
Polymorphism

2.  Follow ALL SOLID principles:
    -   SRP: Each class must have one clear responsibility
    -   OCP: New appenders or formatters must be added without modifying
        existing code
    -   LSP: Subtypes must be substitutable
    -   ISP: Prefer small, focused interfaces
    -   DIP: Depend on abstractions, not implementations
3.  Use Design Patterns where they naturally fit:
    -   Strategy Pattern → Log formatting
    -   Factory Pattern → Appender creation
    -   Chain of Responsibility → Log level filtering
    -   Observer Pattern → Multiple appenders
    -   Facade Pattern → Logger API
    -   Builder Pattern → Log event creation
4.  Favor composition over inheritance
5.  Avoid God objects
6.  Avoid static utility-heavy design

======================== FUNCTIONAL REQUIREMENTS
======================== 1. Log messages with different levels (DEBUG,
INFO, WARN, ERROR) 2. Support multiple appenders (Console, File) 3.
Configure log format 4. Support asynchronous logging 5. Filter logs
based on level 6. Rotate log files 7. Allow multiple loggers

======================== DOMAIN MODEL (EXPECTED)
========================

-   Logger

    -   log(level, message)

-   LogLevel (enum)

    -   DEBUG, INFO, WARN, ERROR

-   LogEvent

    -   timestamp
    -   level
    -   message
    -   threadName

-   Appender (interface)

    -   append(LogEvent)

-   ConsoleAppender

-   FileAppender

-   Formatter (interface)

    -   format(LogEvent)

-   SimpleFormatter

-   JsonFormatter

-   LogFilter

    -   minLevel

-   AsyncLogger

    -   queue-based logging

-   LoggerFactory

    -   creates logger instances

======================== BUSINESS RULES ======================== -
Messages below configured level must be ignored - Multiple appenders
must receive same log - Logging should not block application - Formatter
applied before appender - Async logging optional

======================== CODING STANDARDS (STRICT)
======================== 1. Follow Java naming conventions 2. Classes
must be cohesive and small 3. No public fields 4. Use final where
applicable 5. Use immutable objects where possible 6. Proper access
modifiers 7. No Lombok, no frameworks, no external libraries 8. Avoid
magic numbers 9. Write clean, readable, interview-friendly code

======================== ERROR HANDLING ======================== - Throw
meaningful custom exceptions - Validate inputs at boundaries - Do NOT
silently ignore errors - Handle file writing failures

======================== EXTENSIBILITY EXPECTATIONS
======================== - Adding new appenders must not modify existing
classes - Adding new formatters must be easy - Async logging should be
pluggable - New log levels should be supported

======================== DELIVERABLES ======================== 1.
Complete Java implementation with package structure 2. Clear separation
of concerns 3. Minimal but meaningful comments explaining design
decisions 4. A Main class demonstrating: - Creating logger - Logging
messages - Using multiple appenders - Changing log level - Async logging

======================== INTERVIEW EXPECTATION
======================== - Code should be explainable in 20 minutes -
Trade-offs must be obvious - Design must scale from simple to complex

======================== IMPORTANT RULES ======================== - DO
NOT over-engineer - DO NOT add unnecessary patterns - DO NOT include
unit tests unless asked - Output ONLY Java source code
