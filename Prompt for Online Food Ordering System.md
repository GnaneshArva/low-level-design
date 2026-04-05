Act as a Principal Java Engineer designing a Low Level Design (LLD)
solution for an Online Food Ordering System.

The solution must be interview-grade, production-inspired, and
demonstrate strong object-oriented design, SOLID principles, and design
patterns.

======================== OBJECTIVE ======================== Design and
implement an Online Food Ordering System in Java that supports: -
Restaurant and menu management - Cart operations - Order placement -
Payment handling - Delivery assignment - Order tracking

======================== MANDATORY DESIGN CONSTRAINTS
======================== 1. Apply OOP principles explicitly: -
Encapsulation - Abstraction - Inheritance (only where appropriate) -
Polymorphism

2.  Follow ALL SOLID principles:
    -   SRP: Each class must have one clear responsibility
    -   OCP: New payment methods or delivery strategies must be added
        without modifying existing code
    -   LSP: Subtypes must be substitutable
    -   ISP: Prefer small, focused interfaces
    -   DIP: Depend on abstractions, not implementations
3.  Use Design Patterns where they naturally fit:
    -   State Pattern → Order lifecycle (Created, Paid, Preparing,
        OutForDelivery, Delivered, Cancelled)
    -   Strategy Pattern → Payment handling
    -   Strategy Pattern → Delivery assignment
    -   Factory Pattern → Payment creation
    -   Observer Pattern → Order status updates
    -   Facade Pattern → Order placement API
    -   Builder Pattern → Order creation
4.  Favor composition over inheritance
5.  Avoid God objects
6.  Avoid static utility-heavy design

======================== FUNCTIONAL REQUIREMENTS
======================== 1. Browse restaurants 2. View menu items 3. Add
items to cart 4. Place order 5. Make payment 6. Assign delivery agent 7.
Track order status 8. Cancel order

======================== DOMAIN MODEL (EXPECTED)
========================

-   User
    -   userId
    -   name
    -   address
-   Restaurant
    -   restaurantId
    -   name
    -   menu
-   MenuItem
    -   itemId
    -   name
    -   price
-   Cart
    -   cartItems
-   CartItem
    -   menuItem
    -   quantity
-   Order
    -   orderId
    -   items
    -   status
    -   totalAmount
-   OrderState (interface)
    -   CreatedState
    -   PaidState
    -   PreparingState
    -   OutForDeliveryState
    -   DeliveredState
    -   CancelledState
-   PaymentStrategy (interface)
    -   CardPayment
    -   UpiPayment
    -   WalletPayment
-   DeliveryStrategy (interface)
    -   NearestAgentStrategy
    -   LeastLoadStrategy
-   DeliveryAgent
    -   agentId
    -   availability
-   OrderService
    -   placeOrder()
    -   trackOrder()

======================== BUSINESS RULES ======================== - Order
must have at least one item - Payment required before preparation -
Cancel allowed only before preparation - Delivery assigned after
preparation - Order status flows sequentially

======================== CODING STANDARDS (STRICT)
======================== 1. Follow Java naming conventions 2. Classes
must be cohesive and small 3. No public fields 4. Use final where
applicable 5. Use immutable objects where possible 6. Proper access
modifiers 7. No Lombok, no frameworks, no external libraries 8. Avoid
magic numbers 9. Write clean, readable, interview-friendly code

======================== ERROR HANDLING ======================== - Throw
meaningful custom exceptions - Validate inputs at boundaries - Do NOT
silently ignore errors - Handle payment failure - Handle unavailable
delivery agent

======================== EXTENSIBILITY EXPECTATIONS
======================== - Adding new payment methods must not modify
existing classes - Adding new delivery strategies must not modify
existing classes - Order lifecycle should be extensible - Pricing logic
should evolve independently

======================== DELIVERABLES ======================== 1.
Complete Java implementation with package structure 2. Clear separation
of concerns 3. Minimal but meaningful comments explaining design
decisions 4. A Main class demonstrating: - Creating restaurant and
menu - Adding items to cart - Placing order - Making payment - Assigning
delivery - Tracking order status

======================== INTERVIEW EXPECTATION
======================== - Code should be explainable in 20 minutes -
Trade-offs must be obvious - Design must scale from simple to complex

======================== IMPORTANT RULES ======================== - DO
NOT over-engineer - DO NOT add unnecessary patterns - DO NOT include
unit tests unless asked - Output ONLY Java source code
