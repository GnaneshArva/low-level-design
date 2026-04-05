package moviebooking.state;

import moviebooking.exception.InvalidSeatStateTransitionException;

public class LockedState implements SeatState {

    @Override
    public void lock(SeatContext context) {
        throw new InvalidSeatStateTransitionException("Seat is already locked.");
    }

    @Override
    public void book(SeatContext context) {
        context.setState(new BookedState());
    }

    @Override
    public void release(SeatContext context) {
        context.setState(new AvailableState());
    }

    @Override
    public String getStateName() { return "LOCKED"; }
}
