package com.elevator.state;

import com.elevator.request.ElevatorRequest;

public class IdleState implements ElevatorState {

    @Override
    public void handleRequest(ElevatorRequest request, ElevatorStateContext context) {
        int current = context.getCurrentFloor();
        int target = request.getEffectiveTargetFloor();

        if (target > current) {
            context.setState(new MovingUpState());
        } else if (target < current) {
            context.setState(new MovingDownState());
        }
        // target == current: elevator is already there, no state change needed
    }

    @Override
    public void moveUp(ElevatorStateContext context) {
        context.setState(new MovingUpState());
    }

    @Override
    public void moveDown(ElevatorStateContext context) {
        context.setState(new MovingDownState());
    }

    @Override
    public void stop(ElevatorStateContext context) {
        // already idle — no-op
    }

    @Override
    public String getStateName() {
        return "IDLE";
    }
}
