Act as a Principal Java Engineer designing a Low Level Design (LLD)
solution for a Movie Ticket Booking System.

The solution must be interview-grade, production-inspired, and
demonstrate strong object-oriented design, SOLID principles, and design
patterns.

======================== OBJECTIVE ======================== Design and
implement a Movie Ticket Booking System in Java that supports: - Seat
selection - Concurrent seat booking handling - Payment timeout
handling - Booking confirmation - Cancellation and refunds - Show and
theater management

======================== MANDATORY DESIGN CONSTRAINTS
======================== 1. Apply OOP principles explicitly: -
Encapsulation - Abstraction - Inheritance (only where appropriate) -
Polymorphism

2.  Follow ALL SOLID principles:
    -   SRP: Each class must have one clear responsibility
    -   OCP: New pricing or booking rules must be added without
        modifying existing code
    -   LSP: Subtypes must be substitutable
    -   ISP: Prefer small, focused interfaces
    -   DIP: Depend on abstractions, not implementations
3.  Use Design Patterns where they naturally fit:
    -   State Pattern → Seat state (Available, Locked, Booked)
    -   Strategy Pattern → Pricing calculation
    -   Strategy Pattern → Payment method
    -   Factory Pattern → Payment creation
    -   Facade Pattern → Booking API
    -   Builder Pattern → Booking / receipt creation
4.  Favor composition over inheritance
5.  Avoid God objects
6.  Avoid static utility-heavy design

======================== FUNCTIONAL REQUIREMENTS
======================== 1. Search movie shows 2. Select seats 3. Lock
seats during booking 4. Confirm booking after payment 5. Release seats
on timeout 6. Cancel booking 7. Process refunds 8. Calculate total
ticket price

======================== DOMAIN MODEL (EXPECTED)
========================

-   Movie
    -   movieId
    -   name
    -   duration
-   Theater
    -   theaterId
    -   name
    -   location
    -   screens
-   Screen
    -   screenId
    -   seats
-   Show
    -   showId
    -   movie
    -   startTime
    -   screen
    -   seatMap
-   Seat
    -   seatId
    -   seatType
    -   state
-   SeatState (interface)
    -   AvailableState
    -   LockedState
    -   BookedState
-   Booking
    -   bookingId
    -   selectedSeats
    -   show
    -   bookingStatus
    -   totalAmount
-   PaymentStrategy (interface)
    -   CreditCardPayment
    -   UpiPayment
    -   NetBankingPayment
-   PricingStrategy (interface)
    -   StandardPricing
    -   WeekendPricing
    -   PremiumSeatPricing

======================== BUSINESS RULES ======================== - Seat
must be locked before payment - Locked seats expire after timeout -
Booked seats cannot be rebooked - Payment failure releases seats -
Cancellation allowed only before show start time - Refund processed
after cancellation

======================== CODING STANDARDS (STRICT)
======================== 1. Follow Java naming conventions 2. Classes
must be cohesive and small 3. No public fields 4. Use final where
applicable 5. Use immutable objects where possible 6. Proper access
modifiers 7. No Lombok, no frameworks, no external libraries 8. Avoid
magic numbers 9. Write clean, readable, interview-friendly code

======================== ERROR HANDLING ======================== - Throw
meaningful custom exceptions - Validate inputs at boundaries - Do NOT
silently ignore errors - Handle double booking attempts

======================== EXTENSIBILITY EXPECTATIONS
======================== - Adding new pricing strategies must not modify
existing classes - Adding new payment methods must not modify existing
classes - New seat types should be easy to introduce - Booking workflow
should evolve independently

======================== DELIVERABLES ======================== 1.
Complete Java implementation with package structure 2. Clear separation
of concerns 3. Minimal but meaningful comments explaining design
decisions 4. A Main class demonstrating: - Creating movies and shows -
Selecting seats - Locking seats - Making payment - Confirming booking -
Cancelling booking

======================== INTERVIEW EXPECTATION
======================== - Code should be explainable in 20 minutes -
Trade-offs must be obvious - Design must scale from simple to complex

======================== IMPORTANT RULES ======================== - DO
NOT over-engineer - DO NOT add unnecessary patterns - DO NOT include
unit tests unless asked - Output ONLY Java source code
