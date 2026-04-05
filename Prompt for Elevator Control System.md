Act as a Principal Java Engineer designing a Low Level Design (LLD)
solution for an Elevator Control System. Create a zip file for the
entire source code. The solution must be interview-grade,
production-inspired, and demonstrate strong object-oriented design,
SOLID principles, and design patterns. ========================
OBJECTIVE Design and implement an Elevator Control System in Java that
supports: Multiple elevators Floor requests from inside and outside
elevators Elevator movement and direction management Request scheduling
and assignment Elevator state management Fault handling Priority
requests (e.g., emergency) ======================== MANDATORY DESIGN
CONSTRAINTS Apply OOP principles explicitly:

Encapsulation Abstraction Inheritance (only where appropriate)
Polymorphism Follow ALL SOLID principles:

SRP: Each class must have one clear responsibility OCP: New scheduling
strategies or elevator behaviors must be added without modifying
existing code LSP: Subtypes must be substitutable ISP: Prefer small,
focused interfaces DIP: Depend on abstractions, not implementations Use
Design Patterns where they naturally fit:

State Pattern → Elevator states (Idle, MovingUp, MovingDown,
Maintenance) Strategy Pattern → Elevator scheduling / request assignment
algorithm Observer Pattern → Floor request notifications Factory Pattern
→ Elevator creation Facade Pattern → Simplified API for requesting
elevators Favor composition over inheritance

Avoid God objects

Avoid static utility-heavy design

======================== FUNCTIONAL REQUIREMENTS Handle floor requests
from outside elevators (Up/Down buttons) Handle floor selection from
inside elevators Assign the most appropriate elevator to a request
Manage elevator movement between floors Track elevator direction
(Up/Down/Idle) Maintain elevator states (Idle, Moving, Maintenance)
Handle elevator breakdown / maintenance mode Support priority requests
(e.g., emergency override) ======================== DOMAIN MODEL
(EXPECTED) ElevatorSystem

manages all elevators handles incoming requests Elevator

id currentFloor direction state requestQueue ElevatorController

manages scheduling and elevator assignments ElevatorState (interface)

IdleState MovingUpState MovingDownState MaintenanceState ElevatorRequest

sourceFloor destinationFloor requestType (Internal / External)
RequestScheduler (interface)

NearestElevatorStrategy LoadBalancingStrategy Floor

floorNumber upButton downButton ======================== BUSINESS RULES
Elevator cannot move beyond building floors Elevator cannot accept
requests in maintenance mode Elevator direction should be optimized to
minimize travel time Requests must be processed in an efficient order
Internal requests take precedence once inside elevator
======================== CODING STANDARDS (STRICT) Follow Java naming
conventions Classes must be cohesive and small No public fields Use
final where applicable Use immutable objects where possible Proper
access modifiers No Lombok, no frameworks, no external libraries Avoid
magic numbers Write clean, readable, interview-friendly code
======================== ERROR HANDLING Throw meaningful custom
exceptions Validate inputs at boundaries Do NOT silently ignore errors
======================== EXTENSIBILITY EXPECTATIONS Adding new
scheduling algorithms must not modify existing code Adding new elevator
states must not change core elevator logic New elevator types should be
easy to introduce System should support scaling to many elevators
======================== DELIVERABLES Complete Java implementation with
package structure Clear separation of concerns Minimal but meaningful
comments explaining design decisions A Main class demonstrating:
Creating elevator system Adding elevators Sending floor requests
Elevator movement simulation Printing elevator status
======================== INTERVIEW EXPECTATION Code should be
explainable in 20 minutes Trade-offs must be obvious Design must scale
from simple to complex ======================== IMPORTANT RULES DO NOT
over-engineer DO NOT add unnecessary patterns DO NOT include unit tests
unless asked Output ONLY Java source code

Note: ZIP all the files to a folder.
