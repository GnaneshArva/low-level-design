package moviebooking.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Screen {

    private final String screenId;
    private final String screenName;
    private final List<Seat> seats;

    public Screen(String screenId, String screenName, List<Seat> seats) {
        this.screenId = screenId;
        this.screenName = screenName;
        this.seats = new ArrayList<>(seats); // defensive copy
    }

    public String getScreenId()   { return screenId; }
    public String getScreenName() { return screenName; }

    public List<Seat> getSeats() {
        return Collections.unmodifiableList(seats);
    }
}
