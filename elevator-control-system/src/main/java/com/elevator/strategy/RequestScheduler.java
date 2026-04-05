package com.elevator.strategy;

import com.elevator.core.Elevator;
import com.elevator.request.ElevatorRequest;

import java.util.List;
import java.util.Optional;

/**
 * Strategy pattern: Decouples scheduling algorithm from the controller.
 * New algorithms (zone-based, load-balancing, etc.) implement this interface
 * without touching any existing code. (OCP)
 */
public interface RequestScheduler {

    /**
     * Selects the most appropriate elevator for the given request.
     *
     * @param elevators all available (non-maintenance) elevators
     * @param request   the incoming request
     * @return the chosen elevator, or empty if none available
     */
    Optional<Elevator> selectElevator(List<Elevator> elevators, ElevatorRequest request);
}
