package com.elevator.strategy;

import com.elevator.core.Direction;
import com.elevator.core.Elevator;
import com.elevator.request.ElevatorRequest;

import java.util.List;
import java.util.Optional;

/**
 * Selects the elevator with the smallest distance to the request source floor.
 *
 * Tie-breaking rule: prefer idle elevators over moving ones.
 * This is the default strategy — simple, explainable, interview-friendly.
 *
 * Trade-off: does not consider load; use LoadBalancingStrategy for that.
 */
public class NearestElevatorStrategy implements RequestScheduler {

    @Override
    public Optional<Elevator> selectElevator(List<Elevator> elevators, ElevatorRequest request) {
        return elevators.stream()
            .filter(Elevator::isAvailable)
            .min((a, b) -> {
                int scoreA = score(a, request);
                int scoreB = score(b, request);
                return Integer.compare(scoreA, scoreB);
            });
    }

    /**
     * Lower score = better candidate.
     * Idle elevators get a -1 bonus to break ties.
     */
    private int score(Elevator elevator, ElevatorRequest request) {
        int distance = Math.abs(elevator.getCurrentFloor() - request.getSourceFloor());
        int idleBonus = elevator.getDirection() == Direction.IDLE ? -1 : 0;
        return distance + idleBonus;
    }
}
