package moviebooking.state;

import moviebooking.exception.InvalidSeatStateTransitionException;

public class BookedState implements SeatState {

    @Override
    public void lock(SeatContext context) {
        throw new InvalidSeatStateTransitionException("Seat is already booked.");
    }

    @Override
    public void book(SeatContext context) {
        throw new InvalidSeatStateTransitionException("Seat is already booked.");
    }

    @Override
    public void release(SeatContext context) {
        // Cancellation path — booked → available
        context.setState(new AvailableState());
    }

    @Override
    public String getStateName() { return "BOOKED"; }
}
