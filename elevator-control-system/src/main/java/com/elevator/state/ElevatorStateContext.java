package com.elevator.state;

/**
 * Context interface exposed to state implementations.
 * Limits what states can do to the elevator — ISP in practice.
 */
public interface ElevatorStateContext {

    void setState(ElevatorState newState);

    int getCurrentFloor();

    void setCurrentFloor(int floor);

    int getElevatorId();
}
