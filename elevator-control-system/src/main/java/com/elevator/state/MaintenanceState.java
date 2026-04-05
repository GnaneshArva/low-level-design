package com.elevator.state;

import com.elevator.exception.ElevatorNotAvailableException;
import com.elevator.request.ElevatorRequest;

/**
 * Maintenance state rejects all operations.
 * Demonstrates OCP: new state added without touching Elevator class.
 */
public class MaintenanceState implements ElevatorState {

    @Override
    public void handleRequest(ElevatorRequest request, ElevatorStateContext context) {
        throw new ElevatorNotAvailableException(
            "Elevator " + context.getElevatorId() + " is under maintenance and cannot accept requests."
        );
    }

    @Override
    public void moveUp(ElevatorStateContext context) {
        throw new ElevatorNotAvailableException(
            "Elevator " + context.getElevatorId() + " is under maintenance."
        );
    }

    @Override
    public void moveDown(ElevatorStateContext context) {
        throw new ElevatorNotAvailableException(
            "Elevator " + context.getElevatorId() + " is under maintenance."
        );
    }

    @Override
    public void stop(ElevatorStateContext context) {
        // Already stopped — transitioning out of maintenance requires explicit restore
    }

    @Override
    public String getStateName() {
        return "MAINTENANCE";
    }
}
