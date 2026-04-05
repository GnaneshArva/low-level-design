package moviebooking.service;

import moviebooking.model.Seat;
import moviebooking.model.Show;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SRP: Manages Show registry and available-seat queries.
 * No booking logic here — that lives in BookingService.
 */
public class ShowService {

    // showId → Show
    private final Map<String, Show> shows = new ConcurrentHashMap<>();

    public void addShow(Show show) {
        shows.put(show.getShowId(), show);
    }

    public Show getShow(String showId) {
        Show show = shows.get(showId);
        if (show == null) throw new IllegalArgumentException("Show not found: " + showId);
        return show;
    }

    public List<Show> searchShows(String movieName) {
        List<Show> result = new ArrayList<>();
        for (Show show : shows.values()) {
            if (show.getMovie().getName().equalsIgnoreCase(movieName)) {
                result.add(show);
            }
        }
        return result;
    }

    public List<Seat> getAvailableSeats(String showId) {
        Show show = getShow(showId);
        List<Seat> available = new ArrayList<>();
        for (Seat seat : show.getSeatMap().values()) {
            if (seat.isAvailable()) available.add(seat);
        }
        return available;
    }
}
