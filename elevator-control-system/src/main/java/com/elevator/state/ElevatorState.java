package com.elevator.state;

import com.elevator.request.ElevatorRequest;

/**
 * State pattern: Each concrete state encapsulates behavior for that state.
 * Elevator delegates all state-dependent actions to this interface.
 */
public interface ElevatorState {

    void handleRequest(ElevatorRequest request, ElevatorStateContext context);

    void moveUp(ElevatorStateContext context);

    void moveDown(ElevatorStateContext context);

    void stop(ElevatorStateContext context);

    String getStateName();
}
