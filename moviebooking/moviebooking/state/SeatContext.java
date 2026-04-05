package moviebooking.state;

/**
 * Context object for the State Pattern.
 * Seat delegates moviebooking.state-dependent behavior to the current SeatState.
 */
public class SeatContext {

    private SeatState currentState;

    public SeatContext() {
        this.currentState = new AvailableState();
    }

    public void setState(SeatState state) {
        this.currentState = state;
    }

    public void lock()    { currentState.lock(this); }
    public void book()    { currentState.book(this); }
    public void release() { currentState.release(this); }

    public String getStateName() {
        return currentState.getStateName();
    }

    public boolean isAvailable() { return "AVAILABLE".equals(getStateName()); }
    public boolean isLocked()    { return "LOCKED".equals(getStateName()); }
    public boolean isBooked()    { return "BOOKED".equals(getStateName()); }
}
