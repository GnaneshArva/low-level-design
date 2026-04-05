package moviebooking.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Show maintains its own seatMap so that the same Screen can host
 * multiple shows without seat-moviebooking.state interference.
 * Key design decision: seatMap is cloned per Show at creation time.
 */
public class Show {

    private final String showId;
    private final Movie movie;
    private final Screen screen;
    private final LocalDateTime startTime;

    // seatId → Seat (with its own SeatContext per show)
    private final Map<String, Seat> seatMap;

    public Show(String showId, Movie movie, Screen screen, LocalDateTime startTime) {
        this.showId = showId;
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.seatMap = buildSeatMap(screen);
    }

    /**
     * Each Show gets fresh Seat instances (with AVAILABLE moviebooking.state).
     * We re-create seats rather than share references across shows.
     */
    private Map<String, Seat> buildSeatMap(Screen screen) {
        Map<String, Seat> map = new HashMap<>();
        for (Seat original : screen.getSeats()) {
            Seat freshSeat = new Seat(
                original.getSeatId(),
                original.getRow(),
                original.getNumber(),
                original.getSeatType()
            );
            map.put(freshSeat.getSeatId(), freshSeat);
        }
        return map;
    }

    public String getShowId()           { return showId; }
    public Movie getMovie()             { return movie; }
    public Screen getScreen()           { return screen; }
    public LocalDateTime getStartTime() { return startTime; }

    public Map<String, Seat> getSeatMap() {
        return Collections.unmodifiableMap(seatMap);
    }

    public Seat getSeat(String seatId) {
        Seat seat = seatMap.get(seatId);
        if (seat == null) throw new IllegalArgumentException("Seat not found: " + seatId);
        return seat;
    }

    public boolean hasStarted() {
        return LocalDateTime.now().isAfter(startTime);
    }
}
