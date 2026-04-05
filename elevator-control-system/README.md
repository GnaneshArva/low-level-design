# Elevator Control System — LLD

Interview-grade Low Level Design implementation in pure Java (no frameworks, no Lombok).

---

## Package Structure

```
com.elevator
├── core/
│   ├── Direction.java              # Enum: UP / DOWN / IDLE
│   ├── Elevator.java               # Core domain entity; implements ElevatorStateContext
│   ├── ElevatorController.java     # Orchestrates dispatch and tick loop
│   └── ElevatorObserver.java       # Observer interface for event listeners
├── state/
│   ├── ElevatorState.java          # State interface (State pattern)
│   ├── ElevatorStateContext.java   # ISP-bounded context for state mutations
│   ├── IdleState.java
│   ├── MovingUpState.java
│   ├── MovingDownState.java
│   └── MaintenanceState.java
├── strategy/
│   ├── RequestScheduler.java       # Strategy interface (OCP pivot point)
│   ├── NearestElevatorStrategy.java
│   └── LoadBalancingStrategy.java
├── request/
│   ├── ElevatorRequest.java        # Immutable value object (Builder inside)
│   └── RequestType.java            # Enum: INTERNAL / EXTERNAL / EMERGENCY
├── floor/
│   └── Floor.java                  # Floor with up/down panel buttons
├── factory/
│   └── ElevatorFactory.java        # Factory for elevator construction
├── facade/
│   ├── ElevatorSystem.java         # Facade — the only public API for clients
│   └── ElevatorSystemBuilder.java  # Builder for clean system construction
├── exception/
│   ├── ElevatorNotAvailableException.java
│   ├── InvalidFloorException.java
│   └── NoElevatorAvailableException.java
└── simulation/
    ├── ConsoleObserver.java         # Observer: logs events to console
    └── Main.java                    # 5 demo scenarios
```

---

## Design Patterns Applied

| Pattern     | Where                                         | Why                                                        |
|-------------|-----------------------------------------------|------------------------------------------------------------|
| **State**   | `ElevatorState` + 4 concrete states           | Elevator behavior changes based on state, no if-else chains |
| **Strategy**| `RequestScheduler` + 2 algorithms             | Swap scheduling without touching controller (OCP)          |
| **Observer**| `ElevatorObserver` + `ConsoleObserver`        | Decouple logging/monitoring from core domain               |
| **Factory** | `ElevatorFactory`                             | Centralize elevator creation, enables future variants      |
| **Facade**  | `ElevatorSystem`                              | Single entry point — hides domain complexity from clients  |
| **Builder** | `ElevatorRequest.Builder`, `ElevatorSystemBuilder` | Clean, readable construction of complex objects       |

---

## SOLID Principles

- **SRP**: `Elevator` moves. `ElevatorController` dispatches. `RequestScheduler` selects. `Floor` manages buttons.
- **OCP**: Add `ZoneBasedStrategy` or `ServiceElevatorState` without modifying any existing class.
- **LSP**: All `ElevatorState` implementations are safely substitutable. `MaintenanceState` explicitly rejects operations.
- **ISP**: `ElevatorStateContext` exposes only what states need; states don't access full `Elevator` API.
- **DIP**: `ElevatorController` depends on `RequestScheduler` interface, not `NearestElevatorStrategy`.

---

## Running

```bash
# Build
mvn clean package

# Run
java -jar target/elevator-control-system.jar
```

Or run `Main.java` directly from IntelliJ / any IDE.

---

## Key Trade-offs

| Decision | Trade-off |
|---|---|
| Priority queue (SCAN-order) for normal requests | Good for minimizing travel; doesn't model real SCAN perfectly |
| Tick-based simulation | Simple to reason about; not real-time |
| Emergency queue drains fully before normal | Correct for safety; could starve normal queue under heavy emergency load |
| State stored in Elevator, not externally | Simpler; would need externalization for persistence/distributed scenario |
