Act as a Principal Java Engineer designing a Low Level Design (LLD)
solution for a Parking Lot System.

The solution must be interview-grade, production-inspired, and
demonstrate strong object-oriented design, SOLID principles, and design
patterns.

======================== OBJECTIVE ======================== Design and
implement a Parking Lot System in Java that supports: - Multiple vehicle
types - Slot allocation - Entry and exit handling - Ticket generation -
Parking fee calculation - Real-time availability tracking

======================== MANDATORY DESIGN CONSTRAINTS
======================== 1. Apply OOP principles explicitly: -
Encapsulation - Abstraction - Inheritance (only where appropriate) -
Polymorphism

2.  Follow ALL SOLID principles:
    -   SRP: Each class must have one clear responsibility
    -   OCP: New vehicle types or pricing rules must be added without
        modifying existing code
    -   LSP: Subtypes must be substitutable
    -   ISP: Prefer small, focused interfaces
    -   DIP: Depend on abstractions, not implementations
3.  Use Design Patterns where they naturally fit:
    -   Strategy Pattern → Parking fee calculation
    -   Strategy Pattern → Slot allocation strategy
    -   Factory Pattern → Vehicle creation
    -   State Pattern → Parking slot state (Available, Occupied,
        Reserved)
    -   Facade Pattern → Simplified parking lot API
4.  Favor composition over inheritance
5.  Avoid God objects
6.  Avoid static utility-heavy design

======================== FUNCTIONAL REQUIREMENTS
======================== 1. Park a vehicle 2. Unpark a vehicle 3.
Allocate nearest available slot 4. Generate parking ticket 5. Calculate
parking fee based on duration 6. Track slot availability 7. Support
multiple vehicle types (Car, Bike, Truck) 8. Show available slots per
vehicle type

======================== DOMAIN MODEL (EXPECTED)
========================

-   ParkingLot

    -   manages floors and slots
    -   entry and exit APIs

-   ParkingFloor

    -   floorNumber
    -   collection of parking slots

-   ParkingSlot

    -   slotId
    -   slotType
    -   slotState
    -   assignedVehicle

-   Vehicle (abstract)

    -   vehicleNumber
    -   vehicleType

-   Car extends Vehicle

-   Bike extends Vehicle

-   Truck extends Vehicle

-   Ticket

    -   ticketId
    -   entryTime
    -   exitTime
    -   assignedSlot
    -   vehicle

-   FeeStrategy (interface)

    -   HourlyFeeStrategy
    -   FlatFeeStrategy

-   SlotAllocationStrategy (interface)

    -   NearestSlotStrategy
    -   FirstAvailableStrategy

-   SlotState (interface)

    -   AvailableState
    -   OccupiedState
    -   ReservedState

======================== BUSINESS RULES ======================== - One
vehicle can occupy only one slot - Slot type must match vehicle type -
Parking fee calculated based on entry and exit time - Slot becomes
available after vehicle exits - Cannot unpark without valid ticket - No
double parking allowed

======================== CODING STANDARDS (STRICT)
======================== 1. Follow Java naming conventions 2. Classes
must be cohesive and small 3. No public fields 4. Use final where
applicable 5. Use immutable objects where possible 6. Proper access
modifiers 7. No Lombok, no frameworks, no external libraries 8. Avoid
magic numbers 9. Write clean, readable, interview-friendly code

======================== ERROR HANDLING ======================== - Throw
meaningful custom exceptions - Validate inputs at boundaries - Do NOT
silently ignore errors - Handle full parking lot scenario

======================== EXTENSIBILITY EXPECTATIONS
======================== - Adding new vehicle types must not modify
existing classes - Adding new pricing strategies must not modify
existing classes - Slot allocation algorithms should be pluggable -
Support multiple parking floors easily - Pricing logic should evolve
independently

======================== DELIVERABLES ======================== 1.
Complete Java implementation with package structure 2. Clear separation
of concerns 3. Minimal but meaningful comments explaining design
decisions 4. A Main class demonstrating: - Creating parking lot -
Parking vehicles - Unparking vehicles - Calculating fee - Displaying
available slots

======================== INTERVIEW EXPECTATION
======================== - Code should be explainable in 20 minutes -
Trade-offs must be obvious - Design must scale from simple to complex

======================== IMPORTANT RULES ======================== - DO
NOT over-engineer - DO NOT add unnecessary patterns - DO NOT include
unit tests unless asked - Output ONLY Java source code
