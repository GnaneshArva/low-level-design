package com.elevator.simulation;

import com.elevator.core.Direction;
import com.elevator.facade.ElevatorSystem;
import com.elevator.facade.ElevatorSystemBuilder;
import com.elevator.strategy.LoadBalancingStrategy;

/**
 * Main entry point — demonstrates all system capabilities.
 *
 * Scenarios covered:
 *   1. Basic external floor requests (up/down buttons)
 *   2. Internal cab requests (destination floor selection)
 *   3. Multiple elevators with nearest-elevator scheduling
 *   4. Elevator maintenance mode (fault handling)
 *   5. Emergency priority request
 *   6. Strategy swap to load-balancing
 */
public class Main {

    public static void main(String[] args) {

        separator("SCENARIO 1: Basic External Requests — Nearest Elevator Strategy");
        scenarioBasicExternalRequests();

        separator("SCENARIO 2: Internal Cab Requests");
        scenarioInternalRequests();

        separator("SCENARIO 3: Maintenance Mode — Fault Handling");
        scenarioMaintenance();

        separator("SCENARIO 4: Emergency Priority Request");
        scenarioEmergency();

        separator("SCENARIO 5: Load Balancing Strategy");
        scenarioLoadBalancing();
    }

    // -------------------------------------------------------------------------

    private static void scenarioBasicExternalRequests() {
        // 10-floor building, 3 elevators starting at floors 1, 5, 10
        ElevatorSystem system = new ElevatorSystemBuilder()
            .floors(1, 10)
            .elevators(3)
            .withStartFloors(1, 5, 10)
            .build();

        system.addObserver(new ConsoleObserver());

        System.out.println("Sending external requests...");
        system.requestElevator(3, Direction.UP);   // person on floor 3 wants to go up
        system.requestElevator(8, Direction.DOWN); // person on floor 8 wants to go down

        system.printStatus();

        System.out.println("Running elevators to completion...");
        system.runUntilIdle();

        system.printStatus();
    }

    // -------------------------------------------------------------------------

    private static void scenarioInternalRequests() {
        ElevatorSystem system = new ElevatorSystemBuilder()
            .floors(1, 15)
            .elevators(2)
            .withStartFloors(1, 8)
            .build();

        // External call: person on floor 2 wants up
        system.requestElevator(2, Direction.UP);

        // Elevator 1 (at floor 1) picks up the request.
        // Passenger boards and selects floor 9.
        system.selectDestination(1, 2, 9);

        System.out.println("Running...");
        system.runUntilIdle();

        system.printStatus();
    }

    // -------------------------------------------------------------------------

    private static void scenarioMaintenance() {
        ElevatorSystem system = new ElevatorSystemBuilder()
            .floors(1, 10)
            .elevators(3)
            .withStartFloors(1, 5, 9)
            .build();

        System.out.println("Putting Elevator 2 into maintenance...");
        system.setMaintenanceMode(2);

        // Request should be dispatched to Elevator 1 or 3 (Elevator 2 unavailable)
        system.requestElevator(5, Direction.UP);

        System.out.println("Running...");
        system.runUntilIdle();

        System.out.println("\nRestoring Elevator 2 from maintenance...");
        system.restoreFromMaintenance(2);

        // Now all 3 elevators should be available
        system.requestElevator(5, Direction.DOWN);
        system.runUntilIdle();

        system.printStatus();
    }

    // -------------------------------------------------------------------------

    private static void scenarioEmergency() {
        ElevatorSystem system = new ElevatorSystemBuilder()
            .floors(1, 20)
            .elevators(2)
            .withStartFloors(1, 10)
            .build();

        // Dispatch normal requests first
        system.requestElevator(15, Direction.DOWN);
        system.requestElevator(18, Direction.DOWN);

        // Emergency on floor 7 — should jump ahead of normal queue
        system.triggerEmergency(7);

        System.out.println("Running with emergency active...");
        system.runUntilIdle();

        system.printStatus();
    }

    // -------------------------------------------------------------------------

    private static void scenarioLoadBalancing() {
        // Swap in a different scheduling strategy — no existing code changed (OCP)
        ElevatorSystem system = new ElevatorSystemBuilder()
            .floors(1, 10)
            .elevators(3)
            .withStartFloors(1, 1, 1)  // all elevators start at floor 1
            .withScheduler(new LoadBalancingStrategy())
            .build();

        System.out.println("Dispatching 5 requests with Load Balancing strategy...");
        system.requestElevator(3, Direction.UP);
        system.requestElevator(6, Direction.UP);
        system.requestElevator(9, Direction.DOWN);
        system.requestElevator(2, Direction.UP);
        system.requestElevator(7, Direction.DOWN);

        system.printStatus();
        system.runUntilIdle();
        system.printStatus();
    }

    // -------------------------------------------------------------------------

    private static void separator(String title) {
        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.printf ("║  %-56s║%n", title);
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }
}
