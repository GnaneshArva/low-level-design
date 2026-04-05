package com.elevator.state;

import com.elevator.request.ElevatorRequest;

public class MovingUpState implements ElevatorState {

    @Override
    public void handleRequest(ElevatorRequest request, ElevatorStateContext context) {
        // While moving up, we accept requests — scheduling handles ordering
    }

    @Override
    public void moveUp(ElevatorStateContext context) {
        context.setCurrentFloor(context.getCurrentFloor() + 1);
    }

    @Override
    public void moveDown(ElevatorStateContext context) {
        context.setState(new MovingDownState());
        context.setCurrentFloor(context.getCurrentFloor() - 1);
    }

    @Override
    public void stop(ElevatorStateContext context) {
        context.setState(new IdleState());
    }

    @Override
    public String getStateName() {
        return "MOVING_UP";
    }
}
