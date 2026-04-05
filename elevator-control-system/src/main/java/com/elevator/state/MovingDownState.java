package com.elevator.state;

import com.elevator.request.ElevatorRequest;

public class MovingDownState implements ElevatorState {

    @Override
    public void handleRequest(ElevatorRequest request, ElevatorStateContext context) {
        // While moving down, we accept requests — scheduling handles ordering
    }

    @Override
    public void moveUp(ElevatorStateContext context) {
        context.setState(new MovingUpState());
        context.setCurrentFloor(context.getCurrentFloor() + 1);
    }

    @Override
    public void moveDown(ElevatorStateContext context) {
        context.setCurrentFloor(context.getCurrentFloor() - 1);
    }

    @Override
    public void stop(ElevatorStateContext context) {
        context.setState(new IdleState());
    }

    @Override
    public String getStateName() {
        return "MOVING_DOWN";
    }
}
