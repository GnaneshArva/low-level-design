package com.elevator.factory;

import com.elevator.core.Elevator;

/**
 * Factory pattern: Centralizes elevator construction.
 * Allows future variants (FreightElevator, ServiceElevator) to be introduced
 * without changing client code.
 */
public class ElevatorFactory {

    private ElevatorFactory() {
        // utility — no instances
    }

    public static Elevator createStandard(int id, int minFloor, int maxFloor) {
        return new Elevator(id, minFloor, maxFloor, minFloor);
    }

    public static Elevator createAtFloor(int id, int minFloor, int maxFloor, int startFloor) {
        return new Elevator(id, minFloor, maxFloor, startFloor);
    }
}
