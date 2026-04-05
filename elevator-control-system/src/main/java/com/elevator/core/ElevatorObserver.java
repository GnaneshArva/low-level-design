package com.elevator.core;

import com.elevator.request.ElevatorRequest;

/**
 * Observer pattern: Interested parties (logging, monitoring, UI)
 * react to elevator events without coupling to the core domain.
 */
public interface ElevatorObserver {

    void onRequestAssigned(int elevatorId, ElevatorRequest request);

    void onElevatorStatusChanged(Elevator elevator);
}
