package moviebooking.model;

import moviebooking.state.SeatContext;

/**
 * Seat owns its identity and delegates moviebooking.state management to SeatContext.
 * SRP: Seat is a domain entity, not responsible for moviebooking.state-transition logic.
 */
public class Seat {

    private final String seatId;
    private final String row;
    private final int number;
    private final SeatType seatType;

    // Mutable moviebooking.state managed via State Pattern
    private final SeatContext seatContext;

    public Seat(String seatId, String row, int number, SeatType seatType) {
        if (seatId == null || seatId.isBlank()) throw new IllegalArgumentException("seatId required");
        this.seatId = seatId;
        this.row = row;
        this.number = number;
        this.seatType = seatType;
        this.seatContext = new SeatContext();
    }

    public String getSeatId()    { return seatId; }
    public String getRow()       { return row; }
    public int getNumber()       { return number; }
    public SeatType getSeatType(){ return seatType; }
    public SeatContext getState(){ return seatContext; }

    public boolean isAvailable() { return seatContext.isAvailable(); }

    @Override
    public String toString() {
        return "Seat{" + row + number + "(" + seatType + ")=" + seatContext.getStateName() + "}";
    }
}
