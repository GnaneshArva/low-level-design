package moviebooking.state;

import moviebooking.exception.InvalidSeatStateTransitionException;

public class AvailableState implements SeatState {

    @Override
    public void lock(SeatContext context) {
        context.setState(new LockedState());
    }

    @Override
    public void book(SeatContext context) {
        // Must lock before booking — enforce workflow
        throw new InvalidSeatStateTransitionException("Cannot book without locking first.");
    }

    @Override
    public void release(SeatContext context) {
        // Already available — no-op or guard
        throw new InvalidSeatStateTransitionException("Seat is already available.");
    }

    @Override
    public String getStateName() { return "AVAILABLE"; }
}
