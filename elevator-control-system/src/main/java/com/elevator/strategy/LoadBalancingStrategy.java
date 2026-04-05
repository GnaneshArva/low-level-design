package com.elevator.strategy;

import com.elevator.core.Elevator;
import com.elevator.request.ElevatorRequest;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Selects the elevator with the fewest pending requests.
 *
 * Trade-off: ignores physical distance — may dispatch a distant idle elevator
 * over a nearby busy one. Works well when elevator load varies widely.
 *
 * Demonstrates OCP: plugged in without changing ElevatorController.
 */
public class LoadBalancingStrategy implements RequestScheduler {

    @Override
    public Optional<Elevator> selectElevator(List<Elevator> elevators, ElevatorRequest request) {
        return elevators.stream()
            .filter(Elevator::isAvailable)
            .min(Comparator.comparingInt(Elevator::getPendingRequestCount));
    }
}
