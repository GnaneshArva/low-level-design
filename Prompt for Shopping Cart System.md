Act as a Principal Java Engineer designing a Low Level Design (LLD)
solution for a Shopping Cart System.

The solution must be interview-grade, production-inspired, and
demonstrate strong object-oriented design, SOLID principles, and design
patterns.

======================== OBJECTIVE ======================== Design and
implement a Shopping Cart System in Java that supports: - Adding and
removing items - Updating quantities - Price calculation - Discounts -
Taxes - Checkout readiness

======================== MANDATORY DESIGN CONSTRAINTS
======================== 1. Apply OOP principles explicitly: -
Encapsulation - Abstraction - Inheritance (only where appropriate) -
Polymorphism

2.  Follow ALL SOLID principles:
    -   SRP: Each class must have one clear responsibility
    -   OCP: New discounts/taxes must be added without modifying
        existing code
    -   LSP: Subtypes must be substitutable
    -   ISP: Prefer small, focused interfaces
    -   DIP: Depend on abstractions, not implementations
3.  Use Design Patterns where they naturally fit:
    -   Strategy Pattern → Discount calculation
    -   Strategy Pattern → Tax calculation
    -   Factory Pattern → Creating pricing strategies
    -   Builder Pattern → Final bill / receipt creation
    -   Facade Pattern → Simplified cart checkout API
4.  Favor composition over inheritance
5.  Avoid God objects
6.  Avoid static utility-heavy design

======================== FUNCTIONAL REQUIREMENTS
======================== 1. Add product to cart 2. Remove product from
cart 3. Update quantity of a product 4. Calculate subtotal 5. Apply
discount 6. Apply tax 7. Calculate final payable amount 8. Validate
checkout readiness

======================== DOMAIN MODEL (EXPECTED)
======================== - Product - id (String) - name (String) - price
(BigDecimal)

-   CartItem
    -   Product
    -   quantity (int)
-   ShoppingCart
    -   Collection`<CartItem>`{=html}
    -   CartOperations
-   PricingService
    -   calculateSubtotal()
    -   applyDiscount()
    -   applyTax()
    -   calculateFinalAmount()
-   DiscountStrategy (interface)
    -   PercentageDiscount
    -   FlatDiscount
    -   NoDiscount
-   TaxStrategy (interface)
    -   GSTTax
    -   VATTax
    -   NoTax
-   Bill / Invoice (Immutable)

======================== BUSINESS RULES ======================== -
Quantity must be \> 0 - Cart must not allow checkout if empty -
Discounts and taxes must be optional - Monetary calculations must use
BigDecimal - All validations must fail fast with meaningful exceptions

======================== CODING STANDARDS (STRICT)
======================== 1. Follow Java naming conventions 2. Classes
must be cohesive and small 3. No public fields 4. Use final where
applicable 5. Use immutable objects where possible 6. Proper access
modifiers 7. No Lombok, no frameworks, no external libraries 8. Avoid
magic numbers 9. Write clean, readable, interview-friendly code

======================== ERROR HANDLING ======================== - Throw
meaningful custom exceptions - Validate inputs at boundaries - Do NOT
silently ignore errors

======================== EXTENSIBILITY EXPECTATIONS
======================== - Adding a new DiscountStrategy must not change
existing classes - Adding a new TaxStrategy must not change existing
classes - Pricing rules should evolve without breaking cart logic

======================== DELIVERABLES ======================== 1.
Complete Java implementation with package structure 2. Clear separation
of concerns 3. Minimal but meaningful comments explaining design
decisions 4. A Main class demonstrating: - Add items - Update
quantities - Apply discount - Apply tax - Print final bill

======================== INTERVIEW EXPECTATION
======================== - Code should be explainable in 20 minutes -
Trade-offs must be obvious - Design must scale from simple to complex

======================== IMPORTANT RULES ======================== - DO
NOT over-engineer - DO NOT add unnecessary patterns - DO NOT include
unit tests unless asked - Output ONLY Java source code

Note: ZIP all the files to a folder.