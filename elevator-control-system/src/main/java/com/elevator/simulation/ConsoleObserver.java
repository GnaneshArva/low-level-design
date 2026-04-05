package com.elevator.simulation;

import com.elevator.core.Elevator;
import com.elevator.core.ElevatorObserver;
import com.elevator.request.ElevatorRequest;

/**
 * Observer implementation: logs all elevator events to console.
 * Demonstrates Observer pattern — decoupled from core domain.
 */
public class ConsoleObserver implements ElevatorObserver {

    @Override
    public void onRequestAssigned(int elevatorId, ElevatorRequest request) {
        System.out.printf("[Observer] Request assigned → Elevator %d | %s%n", elevatorId, request);
    }

    @Override
    public void onElevatorStatusChanged(Elevator elevator) {
        System.out.printf("[Observer] Status changed → %s%n", elevator);
    }
}
