package moviebooking.state;

/**
 * State Pattern: Each moviebooking.state encapsulates behavior for transitions.
 * Prevents illegal transitions (e.g., booking an already-booked seat).
 */
public interface SeatState {
    void lock(SeatContext context);
    void book(SeatContext context);
    void release(SeatContext context);
    String getStateName();
}
